package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.ServiceInstanceRepository;
import com.data.udh.dao.StackServiceRoleRepository;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.entity.StackServiceRoleEntity;
import com.data.udh.utils.ShellCommandExecUtil;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CancelHostTagTask  extends BaseUdhTask{
    @Override
    public void internalExecute() {
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);

        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();

        // 查询框架服务角色名获取角色full name
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleFullName = stackServiceRoleEntity.getRoleFullName();

        String hostName = taskParam.getHostName();
        String roleServiceFullName = roleFullName + "-" + serviceInstanceEntity.getServiceName().toLowerCase();

        // kubectl label nodes node003 hadoop-yarn-timelineserver-yarn1-
        ShellCommandExecUtil commandExecUtil = ShellCommandExecUtil.builder().log(log).build();
        String[] command = new String[]{"kubectl", "label","nodes",hostName,roleServiceFullName+"-"};
        log.info("本地执行命令："+ Arrays.stream(command).collect(Collectors.joining(" ")));
        try {
            commandExecUtil.runShellCommandSync("/tmp", command, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
