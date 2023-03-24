package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.StackServiceRoleRepository;
import com.data.udh.entity.StackServiceRoleEntity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ScaleUpK8sServiceTask extends BaseUdhTask {
    @Override
    public void internalExecute() {
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);

        // 查询框架服务角色名获取deployment名字
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleFullName = stackServiceRoleEntity.getRoleFullName();
        String serviceInstanceName = taskParam.getServiceInstanceName();
        String deploymentName = String.format("%s-%s", roleFullName, serviceInstanceName);

        try (KubernetesClient client = new KubernetesClientBuilder().build();) {
            RollableScalableResource<Deployment> resource = client.apps().deployments().inNamespace("default").withName(deploymentName);
            Integer replicas = resource.get().getStatus().getReplicas();
            if (replicas == null) {
                replicas = 0;
            }
            log.info("当前deployment: {} Replicas: {}", deploymentName, replicas);
            int count = replicas + 1;
            log.info("scale up deployment 为: " + count);
            resource.scale(count);
        }


    }
}
