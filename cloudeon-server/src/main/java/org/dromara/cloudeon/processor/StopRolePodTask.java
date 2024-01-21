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

import io.fabric8.kubernetes.api.model.Pod;
import lombok.NoArgsConstructor;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceRoleEntity;
import org.dromara.cloudeon.enums.ServiceRoleState;

import java.util.List;

@NoArgsConstructor
public class StopRolePodTask extends BaseCloudeonTask {
    @Override
    public void internalExecute() {
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        String hostName = taskParam.getHostName();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(taskParam.getServiceInstanceId()).get();

        // 获取集群的namespace
        String namespace = serviceService.getNamespace(serviceInstanceEntity);

        // 查询框架服务角色名获取模板名
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleServiceFullName = serviceService.getRoleServiceFullName(stackServiceRoleEntity, serviceInstanceEntity);
        String podLabel = "app=" + roleServiceFullName;
        kubeService.executeWithKubeClient(serviceInstanceEntity.getClusterId(), client -> {
            List<Pod> pods = client.pods().inNamespace(namespace).withLabel(podLabel).list().getItems();
            for (Pod pod : pods) {
                String nodeName = pod.getSpec().getNodeName();
                if (nodeName != null && nodeName.equals(hostName)) {
                    // do something with the pod
                    String podName = pod.getMetadata().getName();
                    log.info("删除节点 {} 上的pod: {}", hostName, podName);
                    client.pods().delete(pod);
                }
            }
        });
        // 根据hostname查询节点
        Integer nodeId = clusterNodeRepository.findByHostname(hostName).getId();

        // 根据节点id更新角色状态
        ServiceRoleInstanceEntity roleInstanceEntity = serviceRoleInstanceRepository.findByServiceInstanceIdAndNodeIdAndServiceRoleName(serviceInstanceId, nodeId,roleName);
        roleInstanceEntity.setServiceRoleState(ServiceRoleState.ROLE_STOPPED);
        serviceRoleInstanceRepository.save(roleInstanceEntity);
    }
}
