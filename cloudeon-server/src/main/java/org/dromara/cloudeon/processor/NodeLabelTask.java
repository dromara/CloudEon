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

import cn.hutool.core.thread.ThreadUtil;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeBuilder;
import io.fabric8.kubernetes.api.model.NodeFluent;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.NoArgsConstructor;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceRoleEntity;

@NoArgsConstructor
public abstract class NodeLabelTask extends BaseCloudeonTask implements ApplyOrDeleteTask {

    @Override
    public void internalExecute() {
        // 查询框架服务角色名获取模板名
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), taskParam.getRoleName());
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(taskParam.getServiceInstanceId()).get();
        // 拼接成标签
        String tag = serviceService.getRoleServiceFullName(stackServiceRoleEntity, serviceInstanceEntity);
        String hostName = taskParam.getHostName();

        // 调用k8s命令启动资源
        if (isApplyTask()) {
            log.info("给k8s节点 {} 打上label :{}", hostName, tag);
        } else {
            log.info("给k8s节点 {} 移除label :{}", hostName, tag);
        }
        // 增加尝试次数，避免异常： the object has been modified; please apply your changes to the latest version and try again
        int maxRetries = 5;

        kubeService.executeWithKubeClient(serviceInstanceEntity.getClusterId(), client -> {
            boolean executeSuccess = false;
            for (int retryCount = 0; retryCount < maxRetries; retryCount++) {
                // 获取最新的节点信息
                Resource<Node> nodeResource = client.nodes().withName(hostName);
                Node currentNode = nodeResource.get();
                NodeFluent.MetadataNested<NodeBuilder> nodeBuilderMetadataNested = new NodeBuilder(currentNode)
                        .editMetadata()
                        .removeFromLabels(tag);
                if (isApplyTask()) {
                    nodeBuilderMetadataNested.addToLabels(tag, "true");
                }
                Node updatedNode = nodeBuilderMetadataNested.endMetadata()
                        .build();
                try {
                    // 尝试更新节点信息
                    client.nodes().resource(updatedNode).update();
                    log.info("Node information updated successfully");
                    executeSuccess = true;
                    break;  // 如果成功更新，退出重试循环
                } catch (Exception e) {
                    log.error("Error updating node information. Retrying...\n" + e.getMessage());
                    // 等待一段时间后进行重试
                    ThreadUtil.safeSleep(1000);
                }
            }
            if (!executeSuccess) {
                throw new RuntimeException("更新节点信息失败");
            }
        });
    }
}
