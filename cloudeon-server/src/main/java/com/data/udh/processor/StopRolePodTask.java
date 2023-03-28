package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dao.ServiceRoleInstanceRepository;
import com.data.udh.dao.StackServiceRoleRepository;
import com.data.udh.entity.ServiceRoleInstanceEntity;
import com.data.udh.entity.StackServiceRoleEntity;
import com.data.udh.utils.ServiceRoleState;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class StopRolePodTask extends BaseUdhTask {
    @Override
    public void internalExecute() {
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
        ServiceRoleInstanceRepository serviceRoleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);

        String serviceInstanceName = taskParam.getServiceInstanceName();
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        String hostName = taskParam.getHostName();

        // 查询框架服务角色名获取模板名
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleFullName = stackServiceRoleEntity.getRoleFullName();
        String podLabel = String.format("app=%s-%s", roleFullName, serviceInstanceName);
        try (KubernetesClient client = new KubernetesClientBuilder().build();) {

            List<Pod> pods = client.pods().inNamespace("default").withLabel(podLabel).list().getItems();
            for (Pod pod : pods) {
                String nodeName = pod.getSpec().getNodeName();
                if (nodeName != null && nodeName.equals(hostName)) {
                    // do something with the pod
                    String podName = pod.getMetadata().getName();
                    log.info("删除节点 {} 上的pod: {}", hostName, podName);
                    client.pods().withName(podName).delete();
                }
            }
        }
        // 根据hostname查询节点
        Integer nodeId = clusterNodeRepository.findByHostname(hostName).getId();

        // 根据节点id更新角色状态
        ServiceRoleInstanceEntity roleInstanceEntity = serviceRoleInstanceRepository.findByServiceInstanceIdAndNodeIdAndServiceRoleName(serviceInstanceId, nodeId,roleName);
        roleInstanceEntity.setServiceRoleState(ServiceRoleState.ROLE_STOPPED);
        serviceRoleInstanceRepository.save(roleInstanceEntity);
    }
}
