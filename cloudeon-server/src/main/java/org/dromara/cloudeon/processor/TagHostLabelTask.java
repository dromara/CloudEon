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
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.dao.StackServiceRoleRepository;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceRoleEntity;
import org.dromara.cloudeon.service.KubeService;
import io.fabric8.kubernetes.api.model.NodeBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TagHostLabelTask extends BaseCloudeonTask {

    @Override
    public void internalExecute() {
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        KubeService kubeService = SpringUtil.getBean(KubeService.class);

        CloudeonConfigProp cloudeonConfigProp = SpringUtil.getBean(CloudeonConfigProp.class);
        String workHome = cloudeonConfigProp.getWorkHome();
        // 查询框架服务角色名获取模板名
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), taskParam.getRoleName());
        String roleFullName = stackServiceRoleEntity.getRoleFullName();

        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(taskParam.getServiceInstanceId()).get();
        // 获取服务实例名
        String serviceName = serviceInstanceEntity.getServiceName();
        // 拼接成标签
        String tag = roleFullName + "-" + serviceName.toLowerCase();
        String hostName = taskParam.getHostName();

        // 调用k8s命令启动资源
        log.info("给k8s节点 {} 打上label :{}",hostName,tag);
       try(KubernetesClient client = kubeService.getKubeClient(serviceInstanceEntity.getClusterId());){
           // 添加label
           client.nodes().withName(hostName)
                   .edit(r -> new NodeBuilder(r)
                           .editMetadata()
                           .removeFromLabels(tag)
                           .addToLabels(tag, "true")
                           .endMetadata()
                           .build());
       }


    }
}
