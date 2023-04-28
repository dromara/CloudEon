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

import com.fasterxml.jackson.annotation.JsonValue;

public enum ServiceState {
    INIT_SERVICE(1,"新增服务部署中"),
    STOPPING_SERVICE(2,"停止服务中"),
    STARTING_SERVICE(3,"启动服务中"),
    DELETING_SERVICE(4,"删除服务中"),
    RESTARTING_SERVICE(5,"重启服务中"),
    SERVICE_STARTED(6,"服务已启动"),
    SERVICE_STOPPED(7, "服务已停止"),
    ADJUST_SERVICE_ROLE(8, "服务已部署，调整角色实例中");

    private int value;

    private String desc;

    ServiceState(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @JsonValue
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return this.desc;
    }

}
