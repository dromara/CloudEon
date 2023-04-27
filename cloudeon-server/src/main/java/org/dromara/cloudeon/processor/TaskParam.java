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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskParam {
    private Integer commandTaskId;
    private Integer commandId;

    /**
     * 任务关联的服务实例id
     */
    private Integer serviceInstanceId;
    private String serviceInstanceName;
    private Integer stackServiceId;
    private String taskName;
    /**
     * 任务关联的角色名
     */
    private String roleName;
    /**
     * 任务执行的主机host
     */
    private String hostName;
    /**
     * 任务执行的主机ip
     */
    private String ip;


}
