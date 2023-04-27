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

import cn.hutool.extra.spring.SpringUtil;
import org.dromara.cloudeon.service.DeleteClusterService;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DeleteServiceDBDataTask extends BaseCloudeonTask {

    @Override
    public void internalExecute() {
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        DeleteClusterService deleteClusterService = SpringUtil.getBean(DeleteClusterService.class);
        log.info("开始删除 {} 服务相关的表数据....", taskParam.getServiceInstanceName());
        deleteClusterService.deleteOneService(serviceInstanceId);
    }
}
