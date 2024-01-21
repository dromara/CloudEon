/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dromara.cloudeon.controller;

import cn.hutool.core.bean.BeanUtil;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.dromara.cloudeon.controller.request.ModifyClusterInfoRequest;
import org.dromara.cloudeon.controller.response.ClusterInfoVO;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.ResultDTO;
import org.dromara.cloudeon.entity.ClusterAlertRuleEntity;
import org.dromara.cloudeon.entity.ClusterInfoEntity;
import org.dromara.cloudeon.service.KubeService;
import org.dromara.cloudeon.utils.K8sUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.dromara.cloudeon.utils.Constant.AdminUserName;

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
    StackAlertRuleRepository stackAlertRuleRepository;

    @Resource
    private ClusterAlertRuleRepository clusterAlertRuleRepository;

    @Resource
    private KubeService kubeService;

    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
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
        try (KubernetesClient kubernetesClient = K8sUtil.getKubernetesClient(req.getKubeConfig())) {
            kubeService.testConnect(kubernetesClient);

            // 校验 k8s namespace是否存在
            if (!kubeService.checkNamespace(kubernetesClient, req.getNamespace())) {
                return ResultDTO.failed("k8s namespace不存在");
            }
        }

        BeanUtils.copyProperties(req, clusterInfoEntity);
        clusterInfoEntity.setCreateTime(new Date());
        clusterInfoEntity.setCreateBy(AdminUserName);
        clusterInfoEntity.setNamespace(req.getNamespace());

        clusterInfoRepository.saveAndFlush(clusterInfoEntity);
        // 加载默认规则到集群中
        stackAlertRuleRepository.findByStackId(stackId).forEach(stackAlertRuleEntity -> {
            ClusterAlertRuleEntity clusterAlertRuleEntity = new ClusterAlertRuleEntity();
            BeanUtil.copyProperties(stackAlertRuleEntity, clusterAlertRuleEntity);
            clusterAlertRuleEntity.setClusterId(clusterInfoEntity.getId());
            clusterAlertRuleEntity.setCreateTime(new Date());
            clusterAlertRuleEntity.setUpdateTime(new Date());
            clusterAlertRuleRepository.save(clusterAlertRuleEntity);
        });

        return ResultDTO.success(null);
    }


    @PostMapping("/delete")
    @Transactional
    public ResultDTO<String> deleteCluster(Integer id) {
        // 判断服务是否删除
        Integer serviceCnt = serviceInstanceRepository.countByClusterId(id);
        if (serviceCnt > 0) {
            return ResultDTO.failed("请先删除集群下的服务");
        }

        clusterInfoRepository.deleteById(id);
        clusterNodeRepository.deleteByClusterId(id);
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



    @GetMapping("/roleNames")
    public ResultDTO<List<String>> getServiceRoles(Integer clusterId) {
        List<String> names = roleInstanceRepository.findByClusterId(clusterId).stream().map(e -> {
            return e.getServiceRoleName();
        }).distinct().collect(Collectors.toList());
        return ResultDTO.success(names);
    }
}
