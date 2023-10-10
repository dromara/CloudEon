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

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceEntity;
import org.dromara.cloudeon.service.DeleteClusterService;
import lombok.NoArgsConstructor;
import org.dromara.cloudeon.utils.Constant;

import java.io.File;

@NoArgsConstructor
public class DeleteServiceDBDataTask extends BaseCloudeonTask {

    @Override
    public void internalExecute() {
        CloudeonConfigProp cloudeonConfigProp = SpringUtil.getBean(CloudeonConfigProp.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        String workHome = cloudeonConfigProp.getWorkHome();
        String workConfPath = workHome + File.separator + serviceInstanceEntity.getServiceName() ;
        String k8sResourceOutputPath = workHome + File.separator + Constant.K8S_RESOURCE_DIR+File.separator+serviceInstanceEntity.getServiceName() ;
        DeleteClusterService deleteClusterService = SpringUtil.getBean(DeleteClusterService.class);

        log.info("开始删除 {} 服务相关的表数据....", taskParam.getServiceInstanceName());
        deleteClusterService.deleteOneService(serviceInstanceId);

        log.info("开始删除 {} 服务相关的本地数据 {}", taskParam.getServiceInstanceName(),workConfPath);
        log.info("开始删除 {} 服务相关的本地数据 {}", taskParam.getServiceInstanceName(),k8sResourceOutputPath);
        FileUtil.del(workConfPath);
        FileUtil.del(k8sResourceOutputPath);

    }
}
