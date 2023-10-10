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
package org.dromara.cloudeon.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum TaskGroupType {

    CONFIG_SERVICE(3, "配置服务", Lists.newArrayList(TaskType.APPLY_ROLE_CONFIGMAP), false),
    TAG_AND_START_K8S_SERVICE(4, "添加标签并启动k8s服务", Lists.newArrayList(TaskType.TAG_HOST, TaskType.START_K8S_SERVICE), true),
    CANCEL_TAG_AND_STOP_K8S_SERVICE(7, "移除标签并停止k8s服务", Lists.newArrayList(TaskType.CANCEL_TAG_HOST, TaskType.STOP_K8S_SERVICE), true),
    REGISTER_MONITOR(9, "注册监控到Monitor服务", Lists.newArrayList(TaskType.REGISTER_PROMETHEUS), false),
    DELETE_SERVICE(10, "删除服务", Lists.newArrayList(TaskType.DELETE_ROLE_CONFIGMAP), true),
    DELETE_DB_DATA(11, "删除db中服务相关数据", Lists.newArrayList(TaskType.DELETE_SERVICE_DB_DATA), false),
    STOP_ROLE(12, "停止服务的角色实例", Lists.newArrayList(TaskType.CANCEL_TAG_HOST,TaskType.STOP_ROLE_POD,TaskType.SCALE_DOWN_K8S_SERVICE), true),
    START_ROLE(13, "启动服务的角色实例", Lists.newArrayList(TaskType.TAG_HOST,TaskType.SCALE_UP_K8S_SERVICE), true),
    UPDATE_SERVICE_STATE(14, "更新服务实例状态", Lists.newArrayList(TaskType.UPDATE_SERVICE_STATE), false),


    ;

    private final int code;

    private final String name;
    private final List<TaskType> taskTypes;
    private final boolean isRoleLoop;


    public static TaskGroupType of(int code) {
        for (TaskGroupType nodeType : values()) {
            if (nodeType.code == code) {
                return nodeType;
            }
        }
        throw new IllegalArgumentException("unknown TaskGroupType of " + code);
    }

}
