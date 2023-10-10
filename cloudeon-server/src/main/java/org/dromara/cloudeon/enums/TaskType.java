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

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskType {

    TAG_HOST(1, "添加k8s节点标签", "org.dromara.cloudeon.processor.TagHostLabelTask", true),
    START_K8S_SERVICE(2, "启动角色k8s服务", "org.dromara.cloudeon.processor.StartRoleK8sDeploymentTask", false),
    APPLY_ROLE_CONFIGMAP(5, "应用角色相关k8s配置", "org.dromara.cloudeon.processor.ConfigApplyTask", false),
    CANCEL_TAG_HOST(8, "移除节点上的标签", "org.dromara.cloudeon.processor.CancelHostTagTask", true),
    STOP_K8S_SERVICE(9, "停止K8s服务", "org.dromara.cloudeon.processor.StopRoleK8sDeploymentTask", false),
    REGISTER_PROMETHEUS(11, "注册prometheus采集配置", "org.dromara.cloudeon.processor.RegisterPrometheusScrapyTask", false),
    DELETE_ROLE_CONFIGMAP(12, "删除角色相关k8s配置", "org.dromara.cloudeon.processor.ConfigDeleteTask", true),
    DELETE_SERVICE_DB_DATA(13, "删除服务相关db数据库", "org.dromara.cloudeon.processor.DeleteServiceDBDataTask", false),
    STOP_ROLE_POD(14, "停止角色Pod", "org.dromara.cloudeon.processor.StopRolePodTask", true),
    SCALE_DOWN_K8S_SERVICE(15, "按规模减少k8s服务", "org.dromara.cloudeon.processor.ScaleDownK8sServiceTask", false),
    SCALE_UP_K8S_SERVICE(16, "按规模增加k8s服务", "org.dromara.cloudeon.processor.ScaleUpK8sServiceTask", false),
    UPDATE_SERVICE_STATE(17, "更新服务实例状态", "org.dromara.cloudeon.processor.UpdateServiceStateTask", false),


    ;

    private final int code;

    private final String name;

    /**
     * 处理器类
     */
    private final String processorClass;

    private final boolean isHostLoop;

    public static TaskType of(int code) {
        for (TaskType nodeType : values()) {
            if (nodeType.code == code) {
                return nodeType;
            }
        }
        throw new IllegalArgumentException("unknown TaskGroupType of " + code);
    }

}
