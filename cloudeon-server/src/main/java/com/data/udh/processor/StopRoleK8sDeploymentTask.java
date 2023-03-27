package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.config.UdhConfigProp;
import com.data.udh.dao.ServiceInstanceRepository;
import com.data.udh.dao.ServiceRoleInstanceRepository;
import com.data.udh.dao.StackServiceRoleRepository;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.entity.ServiceRoleInstanceEntity;
import com.data.udh.entity.StackServiceRoleEntity;
import com.data.udh.utils.Constant;
import com.data.udh.utils.ServiceRoleState;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * 为角色实例删除k8s deployment
 */
@NoArgsConstructor
public class StopRoleK8sDeploymentTask extends BaseUdhTask {
    @Override
    public void internalExecute() {
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ServiceRoleInstanceRepository serviceRoleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);

        UdhConfigProp udhConfigProp = SpringUtil.getBean(UdhConfigProp.class);
        String workHome = udhConfigProp.getWorkHome();

        // 获取服务实例信息
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();

        // 查询框架服务角色信息
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleFullName = stackServiceRoleEntity.getRoleFullName();

        // 读取本地k8s资源工作目录  ${workHome}/k8s-resource/ZOOKEEPER1/
        String k8sResourceDirPath = workHome + File.separator + Constant.K8S_RESOURCE_DIR + File.separator + serviceInstanceEntity.getServiceName();
        String k8sServiceResourceFilePath = k8sResourceDirPath + File.separator + roleFullName + ".yaml";

        log.info("在k8s上停止deployment ,使用本地资源文件: {}", k8sServiceResourceFilePath);
        try (KubernetesClient client = new KubernetesClientBuilder().build();) {
            client.load(new FileInputStream(k8sServiceResourceFilePath))
                    .inNamespace("default")
                    .delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
