package com.data.cloudeon.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.cloudeon.dao.CommandRepository;
import com.data.cloudeon.dao.ServiceInstanceRepository;
import com.data.cloudeon.entity.CommandEntity;
import com.data.cloudeon.entity.ServiceInstanceEntity;
import com.data.cloudeon.enums.CommandType;
import com.data.cloudeon.enums.ServiceState;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdateServiceStateTask extends BaseCloudeonTask {

    @Override
    public void internalExecute() {
        CommandRepository commandRepository = SpringUtil.getBean(CommandRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);

        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(taskParam.getServiceInstanceId()).get();
        // 根据command type更新服务实例状态、角色实例状态
        Integer commandId = taskParam.getCommandId();
        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        CommandType commandType = commandEntity.getType();
        if (commandType == CommandType.INSTALL_SERVICE || commandType == CommandType.START_SERVICE || commandType == CommandType.RESTART_SERVICE) {
            serviceInstanceEntity.setServiceState(ServiceState.SERVICE_STARTED);

        } else if (commandType == CommandType.STOP_SERVICE) {
            serviceInstanceEntity.setServiceState(ServiceState.SERVICE_STOPPED);
        }
        log.info("更新服务实例 {} 状态为: {}" + serviceInstanceEntity.getServiceName(), serviceInstanceEntity.getServiceState().getDesc());
        serviceInstanceRepository.save(serviceInstanceEntity);

    }
}
