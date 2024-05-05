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

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import lombok.NoArgsConstructor;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceRoleEntity;
import org.dromara.cloudeon.enums.RoleType;
import org.dromara.cloudeon.enums.ServiceRoleState;

@NoArgsConstructor
public abstract class ScaleK8sServiceTask extends BaseCloudeonTask implements ApplyOrDeleteTask {
    @Override
    public void internalExecute() {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(taskParam.getServiceInstanceId()).get();
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);

        RoleType roleType = RoleType.getRoleType(stackServiceRoleEntity.getType());
        if (roleType != RoleType.DEPLOYMENT) {
            return;
        }

        String deploymentName = serviceService.getRoleServiceFullName(stackServiceRoleEntity, serviceInstanceEntity);
        String serviceName = serviceInstanceEntity.getServiceName();

        String tag = serviceService.getRoleFullName(stackServiceRoleEntity) + "-" + serviceName.toLowerCase();

        // 获取集群的namespace
        String namespace = serviceService.getNamespace(serviceInstanceEntity);

        kubeService.executeWithKubeClient(serviceInstanceEntity.getClusterId(), client -> {
            RollableScalableResource<Deployment> resource = client.apps().deployments().inNamespace(namespace).withName(deploymentName);
            Integer replicas = resource.get().getSpec().getReplicas();
            log.info("当前deployment: {} Replicas: {}", deploymentName, replicas);
            if (!isApplyTask()) {
                int count = replicas - 1;
                log.info("scale down deployment 为: " + count);
                resource.scale(count);
            } else {
                if (replicas == null) {
                    replicas = 0;
                }
                int count = replicas + 1;
                log.info("scale up deployment 为: " + count);
                resource.scale(count);

                // 根据label查找启动了角色实例的hostname
                client.nodes().withLabel(tag).list().getItems().forEach(node -> {
                    String hostname = node.getStatus().getAddresses().get(1).getAddress();
                    // 根据hostname查询节点
                    Integer nodeId = clusterNodeRepository.findByHostname(hostname).getId();
                    // 根据节点id更新角色状态
                    ServiceRoleInstanceEntity roleInstanceEntity = serviceRoleInstanceRepository.findByServiceInstanceIdAndNodeIdAndServiceRoleName(serviceInstanceEntity.getId(), nodeId, roleName);
                    roleInstanceEntity.setServiceRoleState(ServiceRoleState.ROLE_STARTED);
                    serviceRoleInstanceRepository.save(roleInstanceEntity);
                });
            }
        });
    }
}
