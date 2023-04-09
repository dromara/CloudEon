package com.data.cloudeon.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.cloudeon.dao.ServiceInstanceRepository;
import com.data.cloudeon.dao.StackServiceRoleRepository;
import com.data.cloudeon.entity.ServiceInstanceEntity;
import com.data.cloudeon.entity.StackServiceRoleEntity;
import com.data.cloudeon.service.KubeService;
import io.fabric8.kubernetes.api.model.NodeBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CancelHostTagTask  extends BaseCloudeonTask {
    @Override
    public void internalExecute() {
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        KubeService kubeService = SpringUtil.getBean(KubeService.class);

        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();

        // 查询框架服务角色名获取角色full name
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleFullName = stackServiceRoleEntity.getRoleFullName();

        String hostName = taskParam.getHostName();
        String roleServiceFullName = roleFullName + "-" + serviceInstanceEntity.getServiceName().toLowerCase();
        // kubectl label nodes node003 hadoop-yarn-timelineserver-yarn1-
        KubernetesClient client = kubeService.getKubeClient(serviceInstanceEntity.getClusterId());
        log.info("给k8s节点 {} 移除label :{}",hostName,roleServiceFullName);
        // 移除lable
        client.nodes().withName(hostName)
                .edit(r -> new NodeBuilder(r)
                        .editMetadata()
                        .removeFromLabels(roleServiceFullName)
                        .endMetadata()
                        .build());
    }
}
