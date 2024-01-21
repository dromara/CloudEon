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

import lombok.NoArgsConstructor;
import org.dromara.cloudeon.entity.CommandEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.enums.CommandType;
import org.dromara.cloudeon.enums.ServiceState;

@NoArgsConstructor
public class UpdateServiceStateTask extends BaseCloudeonTask {

    @Override
    public void internalExecute() {
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
