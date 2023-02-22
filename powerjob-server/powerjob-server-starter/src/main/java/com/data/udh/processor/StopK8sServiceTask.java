package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.config.UdhConfigProp;
import com.data.udh.dao.ServiceInstanceRepository;
import com.data.udh.dao.StackServiceRepository;
import com.data.udh.dao.StackServiceRoleRepository;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.entity.StackServiceEntity;
import com.data.udh.entity.StackServiceRoleEntity;
import com.data.udh.utils.Constant;
import com.data.udh.utils.ShellCommandExecUtil;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor
public class StopK8sServiceTask extends BaseUdhTask {
    @Override
    public void internalExecute() {
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);

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
        String k8sResourceDirPath = workHome + File.separator + Constant.K8S_RESOURCE_DIR+File.separator+serviceInstanceEntity.getServiceName() ;
        String k8sServiceResourceFilePath = k8sResourceDirPath + File.separator + roleFullName + ".yaml";

        ShellCommandExecUtil commandExecUtil = ShellCommandExecUtil.builder().log(log).build();
        String[] command = new String[]{"kubectl", "delete","-f",k8sServiceResourceFilePath,"--ignore-not-found"};
        log.info("本地执行命令："+ Arrays.stream(command).collect(Collectors.joining(" ")));
        try {
            commandExecUtil.runShellCommandSync(k8sResourceDirPath, command, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
