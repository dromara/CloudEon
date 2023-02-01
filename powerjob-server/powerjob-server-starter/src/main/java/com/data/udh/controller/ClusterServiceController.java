package com.data.udh.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.data.udh.controller.request.InitServiceRequest;
import com.data.udh.controller.response.ServiceInstanceVO;
import com.data.udh.dao.*;
import com.data.udh.entity.*;
import com.data.udh.utils.ServiceRoleState;
import com.data.udh.utils.ServiceState;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.powerjob.common.response.ResultDTO;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.data.udh.utils.Constant.AdminUserId;

/**
 * 集群服务相关接口
 */
@RestController
@RequestMapping("/service")
public class ClusterServiceController {

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;

    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @Resource
    private StackServiceRoleRepository stackServiceRoleRepository;

    @Resource
    private StackServiceRepository stackServiceRepository;

    @Resource
    private ServiceRoleInstanceWebuisRepository roleInstanceWebuisRepository;

    @Resource
    private ServiceInstanceConfigRepository serviceInstanceConfigRepository;

    @Resource
    private EntityManager entityManager;

    @Transactional(value = "udhTransactionManager", rollbackFor = Exception.class)
    @PostMapping("/initService")
    public ResultDTO<Void> initService(@RequestBody InitServiceRequest req) {
        Integer clusterId = req.getClusterId();
        Integer stackId = req.getStackId();
        List<InitServiceRequest.ServiceInfo> serviceInfos = req.getServiceInfos();
        // 校验该集群是否已经安装过相同的服务了
        String errorServiceInstanceNames = serviceInfos.stream().map(info -> {
            ServiceInstanceEntity sameStackServiceInstance = serviceInstanceRepository.findByClusterIdAndStackServiceId(clusterId, info.getStackServiceId());
            if (sameStackServiceInstance != null) {
                return sameStackServiceInstance.getServiceName();
            }
            return null;
        }).filter(StrUtil::isNotBlank).collect(Collectors.joining(","));

        if (StrUtil.isNotBlank(errorServiceInstanceNames)) {
            return ResultDTO.failed("该集群已经安装过相同的服务实例：" + errorServiceInstanceNames);
        }
        for (InitServiceRequest.ServiceInfo serviceInfo : serviceInfos) {

            Integer stackServiceId = serviceInfo.getStackServiceId();
            // 查询实例表获取新增的实例序号
            Integer maxInstanceSeq = (Integer) entityManager.createNativeQuery("select max(instance_sequence) from udh_service_instance where stack_service_id = 1=" + stackServiceId).getSingleResult();
            if (maxInstanceSeq == null) {
                maxInstanceSeq = 0;
            }
            Integer newInstanceSeq = maxInstanceSeq + 1;

            ServiceInstanceEntity serviceInstanceEntity = new ServiceInstanceEntity();
            serviceInstanceEntity.setInstanceSequence(newInstanceSeq);
            serviceInstanceEntity.setSid(serviceInfo.getStackServiceName() + newInstanceSeq);
            serviceInstanceEntity.setServiceName(serviceInfo.getStackServiceName() + newInstanceSeq);
            serviceInstanceEntity.setLabel(serviceInfo.getStackServiceLabel());
            serviceInstanceEntity.setClusterId(clusterId);
            serviceInstanceEntity.setCreateTime(new Date());
            serviceInstanceEntity.setUpdateTime(new Date());
            serviceInstanceEntity.setEnableKerberos(req.getEnableKerberos());
            serviceInstanceEntity.setStackServiceId(stackServiceId);
            serviceInstanceEntity.setServiceState(ServiceState.OPERATING);

            // 持久化service信息
            serviceInstanceRepository.save(serviceInstanceEntity);
            // 获取持久化后的service 实例id
            Integer serviceInstanceEntityId = serviceInstanceEntity.getId();

            // 获取service 所有角色
            List<InitServiceRequest.InitServiceRole> roles = serviceInfo.getRoles();
            for (InitServiceRequest.InitServiceRole role : roles) {
                String stackRoleName = role.getStackRoleName();
                StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(stackServiceId, stackRoleName);
                if (stackServiceRoleEntity == null) {
                    throw new IllegalArgumentException("can't find stack service role by role name:" + stackRoleName + " and stack service id: " + stackServiceId);
                }

                // 遍历该角色分布的节点，生成serviceRoleInstanceEntities
                List<ServiceRoleInstanceEntity> serviceRoleInstanceEntities = role.getNodeIds().stream().map(new Function<Integer, ServiceRoleInstanceEntity>() {
                    @Override
                    public ServiceRoleInstanceEntity apply(Integer nodeId) {
                        ServiceRoleInstanceEntity roleInstanceEntity = new ServiceRoleInstanceEntity();
                        roleInstanceEntity.setClusterId(clusterId);
                        roleInstanceEntity.setCreateTime(new Date());
                        roleInstanceEntity.setUpdateTime(new Date());
                        roleInstanceEntity.setServiceInstanceId(serviceInstanceEntityId);
                        roleInstanceEntity.setStackServiceRoleId(stackServiceRoleEntity.getId());
                        roleInstanceEntity.setServiceRoleName(stackRoleName);
                        roleInstanceEntity.setServiceRoleState(ServiceRoleState.OPERATING);
                        roleInstanceEntity.setNodeId(nodeId);
                        return roleInstanceEntity;
                    }
                }).collect(Collectors.toList());

                // 批量持久化role实例
                List<ServiceRoleInstanceEntity> serviceRoleInstanceEntitiesAfter = roleInstanceRepository.saveAllAndFlush(serviceRoleInstanceEntities);

                // 为每个角色分布的节点，都生成service RoleUi地址
                List<ServiceRoleInstanceWebuisEntity> roleInstanceWebuisEntities = serviceRoleInstanceEntitiesAfter.stream().map(new Function<ServiceRoleInstanceEntity, ServiceRoleInstanceWebuisEntity>() {
                    @Override
                    public ServiceRoleInstanceWebuisEntity apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                        String roleLinkExpression = stackServiceRoleEntity.getLinkExpression();
                        // 持久化service Role UI信息
                        ServiceRoleInstanceWebuisEntity serviceRoleInstanceWebuisEntity = new ServiceRoleInstanceWebuisEntity();
                        serviceRoleInstanceWebuisEntity.setName("UI地址");
                        serviceRoleInstanceWebuisEntity.setServiceInstanceId(serviceInstanceEntityId);
                        serviceRoleInstanceWebuisEntity.setServiceRoleInstanceId(serviceRoleInstanceEntity.getServiceInstanceId());
                        serviceRoleInstanceWebuisEntity.setWebHostUrl(roleLinkExpression);
                        serviceRoleInstanceWebuisEntity.setWebIpUrl(roleLinkExpression);
                        return serviceRoleInstanceWebuisEntity;
                    }
                }).collect(Collectors.toList());

                // 批量持久化role web ui
                roleInstanceWebuisRepository.saveAll(roleInstanceWebuisEntities);


            }

            List<InitServiceRequest.InitServicePresetConf> presetConfList = serviceInfo.getPresetConfList();
            List<ServiceInstanceConfigEntity> serviceInstanceConfigEntities = presetConfList.stream().map(new Function<InitServiceRequest.InitServicePresetConf, ServiceInstanceConfigEntity>() {
                @Override
                public ServiceInstanceConfigEntity apply(InitServiceRequest.InitServicePresetConf initServicePresetConf) {
                    ServiceInstanceConfigEntity serviceInstanceConfigEntity = new ServiceInstanceConfigEntity();
                    BeanUtil.copyProperties(initServicePresetConf, serviceInstanceConfigEntity);
                    serviceInstanceConfigEntity.setUpdateTime(new Date());
                    serviceInstanceConfigEntity.setCreateTime(new Date());
                    serviceInstanceConfigEntity.setServiceInstanceId(serviceInstanceEntityId);
                    serviceInstanceConfigEntity.setUserId(AdminUserId);
                    return serviceInstanceConfigEntity;
                }
            }).collect(Collectors.toList());

            // 批量持久化service Conf信息
            serviceInstanceConfigRepository.saveAll(serviceInstanceConfigEntities);

        }

        // 根据需要安装的服务在实例表中找到依赖的服务id，并更新service信息
        List<Integer> stackServiceIds = req.getServiceInfos().stream().map(e -> e.getStackServiceId()).collect(Collectors.toList());
        // 过滤出有依赖的服务
        List<StackServiceEntity> installStackServiceEntities = stackServiceRepository.findAllById(stackServiceIds).stream().filter(e -> StrUtil.isNotBlank(e.getDependencies())).collect(Collectors.toList());
        for (StackServiceEntity stackServiceEntity : installStackServiceEntities) {
            String[] depStr = stackServiceEntity.getDependencies().split(",");
            List<String> depInstanceIds = Arrays.stream(depStr).map(new Function<String, String>() {
                @Override
                public String apply(String depStackServiceName) {
                    //查找集群内该服务依赖的服务实例
                    Integer depServiceInstanceId = serviceInstanceRepository.findByClusterIdAndStackServiceName(clusterId, depStackServiceName);
                    return depServiceInstanceId + "";
                }
            }).collect(Collectors.toList());

            String depInstanceIdStr = StrUtil.join(",", depInstanceIds);
            // 更新需要安装的服务实例，将依赖服务实例id写入
            ServiceInstanceEntity updateServiceInstanceEntity = serviceInstanceRepository.findByClusterIdAndStackServiceId(clusterId, stackServiceEntity.getId());
            updateServiceInstanceEntity.setDependenceServiceInstanceIds(depInstanceIdStr);
            serviceInstanceRepository.save(updateServiceInstanceEntity);
        }


        // todo 生成新增服务command和调用workflow


        return ResultDTO.success(null);
    }


    /**
     * 校验要安装的服务是否需要Kerberos配置
     */
    @PostMapping("/validInstallServiceHasKerberos")
    ResultDTO<Boolean> installServiceHasKerberos(@RequestBody List<Integer> InstallStackServiceIds) {
        for (StackServiceEntity stackServiceEntity : stackServiceRepository.findAllById(InstallStackServiceIds)) {
            if (stackServiceEntity.isSupportKerberos()) {
                return ResultDTO.success(true);
            }
        }
        return ResultDTO.success(false);
    }


    /**
     * 服务实例列表
     */
    @GetMapping("/listServiceInstance")
    public ResultDTO<List<ServiceInstanceVO>> listServiceInstance(Integer clusterId) {
        List<ServiceInstanceVO> result = serviceInstanceRepository.findByClusterId(clusterId).stream().map(instanceEntity -> {
            ServiceInstanceVO serviceInstanceVO = new ServiceInstanceVO();
            BeanUtil.copyProperties(instanceEntity, serviceInstanceVO);
            ServiceState serviceState = instanceEntity.getServiceState();
            serviceInstanceVO.setServiceStateValue(serviceState.name());

            // 根据状态查询icon
            StackServiceEntity stackServiceEntity = stackServiceRepository.findById(instanceEntity.getStackServiceId()).get();
            if (serviceState == ServiceState.OPERATING) {
                serviceInstanceVO.setIcon(stackServiceEntity.getIconApp());
            } else if (serviceState == ServiceState.WARN || serviceState == ServiceState.DANGER) {
                serviceInstanceVO.setIcon(stackServiceEntity.getIconDanger());
            } else {
                serviceInstanceVO.setIcon(stackServiceEntity.getIconDefault());
            }

            return serviceInstanceVO;
        }).collect(Collectors.toList());


        return ResultDTO.success(result);
    }
}
