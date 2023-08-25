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
package org.dromara.cloudeon.processor;

import cn.hutool.extra.spring.SpringUtil;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.ClusterInfoRepository;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.dao.ServiceRoleInstanceRepository;
import org.dromara.cloudeon.dao.StackServiceRoleRepository;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceRoleEntity;
import org.dromara.cloudeon.service.KubeService;
import org.dromara.cloudeon.utils.Constant;
import org.dromara.cloudeon.enums.ServiceRoleState;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 为角色实例删除k8s deployment
 */
@NoArgsConstructor
public class StopRoleK8sDeploymentTask extends BaseCloudeonTask {
    @Override
    public void internalExecute() {
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ServiceRoleInstanceRepository serviceRoleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
        KubeService kubeService = SpringUtil.getBean(KubeService.class);
        ClusterInfoRepository clusterInfoRepository = SpringUtil.getBean(ClusterInfoRepository.class);

        CloudeonConfigProp cloudeonConfigProp = SpringUtil.getBean(CloudeonConfigProp.class);
        String workHome = cloudeonConfigProp.getWorkHome();

        // 获取服务实例信息
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        // 获取集群的namespace
        String namespace = clusterInfoRepository.findById(serviceInstanceEntity.getClusterId()).get().getNamespace();
        if (StringUtils.isBlank(namespace)) {
            namespace = "default";
        }

        // 查询框架服务角色信息
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleFullName = stackServiceRoleEntity.getRoleFullName();

        // 读取本地k8s资源工作目录  ${workHome}/k8s-resource/ZOOKEEPER1/
        String k8sResourceDirPath = workHome + File.separator + Constant.K8S_RESOURCE_DIR + File.separator + serviceInstanceEntity.getServiceName();
        String k8sServiceResourceFilePath = k8sResourceDirPath + File.separator + roleFullName + ".yaml";
        log.info("本地资源文件: {}", k8sServiceResourceFilePath);

        // 判断k8s资源文件是否存在
        if (new File(k8sServiceResourceFilePath).exists()) {
            log.info("在k8s上停止deployment ,使用本地资源文件: {}", k8sServiceResourceFilePath);
            try (KubernetesClient client = kubeService.getKubeClient(serviceInstanceEntity.getClusterId());) {
                client.load(new FileInputStream(k8sServiceResourceFilePath))
                        .inNamespace(namespace)
                        .delete();
                // 等待deployment完全停止
                RollableScalableResource<Deployment> deploymentRollableScalableResource = client.apps().deployments()
                        .inNamespace(namespace)
                        .withName(roleFullName);
                if (deploymentRollableScalableResource != null) {
                    log.info("等待deployment完全停止: {}", roleFullName);
                    deploymentRollableScalableResource
                            .waitUntilCondition(d -> d.getStatus().getReadyReplicas() == 0, 10, TimeUnit.MINUTES);
                }


            } catch (FileNotFoundException e) {
                log.error("k8s资源文件不存在: {}", k8sServiceResourceFilePath);
                throw new RuntimeException(e);
            }

            // 更新角色实例状态为已停止
            List<ServiceRoleInstanceEntity> roleInstanceEntities = serviceRoleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId, stackServiceRoleEntity.getName());
            roleInstanceEntities.forEach(r->{
                r.setServiceRoleState(ServiceRoleState.ROLE_STOPPED);
                serviceRoleInstanceRepository.save(r);
            });

        }
    }
}
