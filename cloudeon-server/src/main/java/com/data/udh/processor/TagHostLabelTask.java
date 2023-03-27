package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.config.UdhConfigProp;
import com.data.udh.dao.ServiceInstanceRepository;
import com.data.udh.dao.StackServiceRoleRepository;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.entity.StackServiceRoleEntity;
import com.data.udh.utils.ShellCommandExecUtil;
import io.fabric8.kubernetes.api.model.NodeBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor
public class TagHostLabelTask extends BaseUdhTask {

    @Override
    public void internalExecute() {
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        UdhConfigProp udhConfigProp = SpringUtil.getBean(UdhConfigProp.class);
        String workHome = udhConfigProp.getWorkHome();
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
       try(KubernetesClient client = new KubernetesClientBuilder().build();){
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
