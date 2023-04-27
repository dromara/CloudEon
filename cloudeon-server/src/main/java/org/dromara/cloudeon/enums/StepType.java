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
public enum StepType {

    INSTALL_SERVICE_STEP(1,"安装",Lists.newArrayList(CommandType.INSTALL_SERVICE.name())),

    CONFIG_SERVICE_STEP(2,"配置",Lists.newLinkedList()),

    INIT_SERVICE_STEP(3,"初始化",Lists.newLinkedList()),

    START_SERVICE_STEP(4,"启动", Lists.newLinkedList()),




    ;

    private final int code;

    private final String name;
    private final List<String> list;

    public static StepType of(int code) {
        for (StepType nodeType : values()) {
            if (nodeType.code == code) {
                return nodeType;
            }
        }
        throw new IllegalArgumentException("unknown TaskGroupType of " + code);
    }

}
