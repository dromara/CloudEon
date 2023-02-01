package com.data.udh.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.data.udh.controller.request.ValidServicesDepRequest;
import com.data.udh.controller.response.StackServiceConfVO;
import com.data.udh.controller.response.StackServiceVO;
import com.data.udh.dao.*;
import com.data.udh.dto.StackConfiguration;
import com.data.udh.entity.*;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;
import tech.powerjob.common.response.ResultDTO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stack Controller
 * vue axios 的POST请求必须使用 @RequestBody 接收
 */
@RestController
@RequestMapping("/stack")
public class StackController {

    @Resource
    private StackInfoRepository stackInfoRepository;

    @Resource
    private ClusterInfoRepository clusterInfoRepository;

    @Resource
    private StackServiceRepository serviceRepository;

    @Resource
    private StackServiceConfRepository serviceConfRepository;

    @Resource
    private StackServiceRoleRepository serviceRoleRepository;

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;


    @GetMapping("/list")
    public ResultDTO<List<StackInfoEntity>> listStackInfo() {
        List<StackInfoEntity> result;
        result = stackInfoRepository.findAll();
        return ResultDTO.success(result);
    }

    /**
     * 根据clusterID查询框架包含的服务信息
     */
    @GetMapping("/listService")
    public ResultDTO<List<StackServiceVO>> listService(Integer clusterId) {
        List<StackServiceVO> result;
        // 根据集群id查询绑定的stackId
        Integer stackId = clusterInfoRepository.findById(clusterId).orElseThrow(() -> new IllegalArgumentException("无法找到id=" + clusterId + " 的集群")).getStackId();

        result = serviceRepository.findByStackId(stackId).stream().map(e -> {
            StackServiceVO stackServiceVO = new StackServiceVO();
            BeanUtil.copyProperties(e, stackServiceVO);
            // 查找该集群是否已经安装过该服务
            ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findByClusterIdAndStackServiceId(clusterId, e.getId());
            stackServiceVO.setInstalledInCluster(serviceInstanceEntity != null);
            // 查找该服务含的角色
            List<StackServiceRoleEntity> roleEntities = serviceRoleRepository.findByServiceIdAndStackId(e.getId(), stackId);
            List<String> roleNames = roleEntities.stream().map(StackServiceRoleEntity::getName).collect(Collectors.toList());
            stackServiceVO.setRoles(roleNames);

            return stackServiceVO;
        }).collect(Collectors.toList());
        return ResultDTO.success(result);
    }

    /**
     * 查询服务的可配置参数
     */
    @GetMapping("/listServiceConf")
    public ResultDTO<StackServiceConfVO> listServiceConf(Integer serviceId, boolean inWizard) {
        StackServiceConfVO result = new StackServiceConfVO();
        // 校验服务是否存在
        StackServiceEntity stackServiceEntity = serviceRepository.findById(serviceId).orElseThrow(() -> new IllegalArgumentException("无法找到id=" + serviceId + " 的service"));
        // 数据库中查询该框架该服务的配置
        List<StackServiceConfEntity> stackServiceConfEntities;
        if (inWizard) {
            stackServiceConfEntities = serviceConfRepository.findByServiceIdAndConfigurableInWizard(serviceId, inWizard);
        } else {
            stackServiceConfEntities = serviceConfRepository.findByServiceId(serviceId);
        }
        // covert entity to dto
        List<StackConfiguration> stackConfigurations = stackServiceConfEntities.stream().map(stackServiceConfEntity -> {
            StackConfiguration stackConfiguration = new StackConfiguration();
            BeanUtil.copyProperties(stackServiceConfEntity, stackConfiguration);
            ArrayList<String> groups = ListUtil.toList(stackServiceConfEntity.getGroups().split(","));
            stackConfiguration.setGroups(groups);
            return stackConfiguration;
        }).collect(Collectors.toList());

        // 查找该服务的自定义配置文件
        ArrayList<String> customFileNames = ListUtil.toList(stackServiceEntity.getCustomConfigFiles().split(","));

        result.setConfs(stackConfigurations);
        result.setCustomFileNames(customFileNames);
        return ResultDTO.success(result);
    }

    /**
     * 校验要安装的services的依赖是否正确
     * 示例 ======
     * 要安装：YARN、HDFS
     * 解析出依赖：ZK、HDFS
     * 提示：需要先安装ZK
     */
    @PostMapping("/validInstallServicesDeps")
    public ResultDTO<Void> validInstallServicesDeps(@RequestBody ValidServicesDepRequest request) {
        // 获取需要安装的服务id
        List<Integer> installStackServiceIds = request.getInstallStackServiceIds();
        // 校验该集群是否已经安装过相同的服务了
        String errorServiceInstanceNames = installStackServiceIds.stream().map(id -> {
            ServiceInstanceEntity sameStackServiceInstance = serviceInstanceRepository.findByClusterIdAndStackServiceId(request.getClusterId(), id);
            if (sameStackServiceInstance != null) {
                return sameStackServiceInstance.getServiceName();
            }
            return null;
        }).filter(StrUtil::isNotBlank).collect(Collectors.joining(","));

        if (StrUtil.isNotBlank(errorServiceInstanceNames)) {
            return ResultDTO.failed("该集群已经安装过相同的服务实例：" + errorServiceInstanceNames);
        }

        // 从数据库查询这些服务
        List<StackServiceEntity> stackServiceEntities = serviceRepository.findAllById(installStackServiceIds);
        // 获取这次要安装的服务名列表
        List<String> installServiceNames = stackServiceEntities.stream().map(StackServiceEntity::getName).collect(Collectors.toList());
        // 去重获取这次要安装的服务需要依赖的服务名
        List<String> depServiceNames = stackServiceEntities
                .stream()
                // 去除""值
                .filter(s -> StrUtil.isNotBlank(s.getDependencies()))
                .flatMap(new Function<StackServiceEntity, Stream<String>>() {
                    @Override
                    public Stream<String> apply(StackServiceEntity stackServiceEntity) {
                        return Arrays.stream(stackServiceEntity.getDependencies().split(","));
                    }
                })
                .distinct()
                .collect(Collectors.toList());

        List<String> needPreInstallServiceNames = depServiceNames
                .stream()
                .filter(dep -> {
                    // 检查这次要安装的服务中是否包含了依赖的服务，如果有可以去掉该依赖服务
                    return !installServiceNames.contains(dep);
                })
                .filter(dep -> {
                    // 检查该集群是否已经安装过依赖的框架服务
                    Integer clusterId = request.getClusterId();
                    Integer stackId = request.getStackId();
                    // 查询依赖框架服务的id
                    Integer depStackServiceId = serviceRepository.findByStackIdAndName(stackId, dep).getId();
                    ServiceInstanceEntity query = new ServiceInstanceEntity();
                    query.setClusterId(clusterId);
                    query.setStackServiceId(depStackServiceId);
                    // 查询集群已安装的依赖服务实例
                    long count = serviceInstanceRepository.count(Example.of(query));
                    return count <= 0;
                })
                .collect(Collectors.toList());

        if (needPreInstallServiceNames.size() > 0) {
            return ResultDTO.failed("需要提前安装服务：" + StrUtil.join(",", needPreInstallServiceNames));
        }

        return ResultDTO.success(null);
    }

}
