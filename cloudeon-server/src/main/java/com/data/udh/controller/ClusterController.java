package com.data.udh.controller;

import com.data.udh.controller.request.ModifyClusterInfoRequest;
import com.data.udh.controller.response.ClusterInfoVO;
import com.data.udh.dao.ClusterInfoRepository;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dao.ServiceInstanceRepository;
import com.data.udh.dao.StackInfoRepository;
import com.data.udh.dto.ResultDTO;
import com.data.udh.entity.ClusterInfoEntity;
import com.data.udh.service.KubeService;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.data.udh.utils.Constant.AdminUserName;

/**
 * Cluster Controller
 * vue axios 的POST请求必须使用 @RequestBody 接收
 *
 */
@RestController
@RequestMapping("/cluster")
public class ClusterController {


    @Resource
    private ClusterInfoRepository clusterInfoRepository;

    @Resource
    private StackInfoRepository stackInfoRepository;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;
    @Resource
    private KubeService kubeService;

    @PostMapping("/save")
    public ResultDTO<Void> saveCluster(@RequestBody ModifyClusterInfoRequest req) {

        ClusterInfoEntity clusterInfoEntity;

        // 检查框架id是否存在
        Integer stackId = req.getStackId();
        stackInfoRepository.findById(stackId).orElseThrow(() -> new IllegalArgumentException("can't find stack info by stack Id:" + stackId));;

        Integer id = req.getId();
        // 判断更新还是新建
        if (id == null) {
            clusterInfoEntity = new ClusterInfoEntity();
            clusterInfoEntity.setCreateTime(new Date());
        }else {
            clusterInfoEntity = clusterInfoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("can't find cluster info by id:" + id));
        }
        // 检验kubeconfig是否正确能连接k8s集群
        KubernetesClient kubernetesClient = kubeService.getKubernetesClient(req.getKubeConfig());
        kubeService.testConnect(kubernetesClient);
        BeanUtils.copyProperties(req, clusterInfoEntity);
        clusterInfoEntity.setCreateTime(new Date());
        clusterInfoEntity.setCreateBy(AdminUserName);

        clusterInfoRepository.saveAndFlush(clusterInfoEntity);
        return ResultDTO.success(null);
    }


    @GetMapping("/delete")
    public ResultDTO<Void> deleteCluster(Integer appId) {
        // todo 删除前确认节点和服务都删除了
        clusterInfoRepository.deleteById(appId);
        return ResultDTO.success(null);
    }

    @GetMapping("/list")
    public ResultDTO<List<ClusterInfoVO>> listClusterInfo() {
        List<ClusterInfoVO> clusterInfoVOS = clusterInfoRepository.findAll().stream().map(new Function<ClusterInfoEntity, ClusterInfoVO>() {
            @Override
            public ClusterInfoVO apply(ClusterInfoEntity clusterInfoEntity) {
                ClusterInfoVO clusterInfoVO = new ClusterInfoVO();
                Integer clusterId = clusterInfoEntity.getId();
                BeanUtils.copyProperties(clusterInfoEntity,clusterInfoVO);
                // 查询节点数
                Integer nodeCnt = clusterNodeRepository.countByClusterId(clusterId);
                // 查询服务数
                Integer serviceCnt = serviceInstanceRepository.countByClusterId(clusterId);
                clusterInfoVO.setNodeCnt(nodeCnt);
                clusterInfoVO.setServiceCnt(serviceCnt);
                return clusterInfoVO;
            }
        }).collect(Collectors.toList());
        return ResultDTO.success(clusterInfoVOS);
    }




}
