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
package org.dromara.cloudeon.service;

import org.dromara.cloudeon.dao.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class DeleteClusterService {
    @Resource
    ServiceInstanceRepository serviceInstanceRepository ;
    @Resource
    ServiceInstanceConfigRepository serviceInstanceConfigRepository;
    @Resource
    ServiceRoleInstanceRepository roleInstanceRepository ;
    @Resource
    ServiceRoleInstanceWebuisRepository roleInstanceWebuisRepository;
    @Resource
    private AlertMessageRepository alertMessageRepository;

    @Transactional(rollbackFor = Exception.class)
    public void deleteOneService(Integer serviceInstanceId) {
        // 删除服务实例表
        serviceInstanceRepository.deleteById(serviceInstanceId);
        // 删除服务角色实例表
        roleInstanceRepository.deleteByServiceInstanceId(serviceInstanceId);
        // 删除服务角色配置表
        serviceInstanceConfigRepository.deleteByServiceInstanceId(serviceInstanceId);
        // 删除服务ui表
        roleInstanceWebuisRepository.deleteByServiceInstanceId(serviceInstanceId);
        // 删除告警信息表
        alertMessageRepository.deleteByServiceInstanceId(serviceInstanceId);

    }
}
