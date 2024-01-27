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

public enum CommandState {
    /**
     * 等待
     */
    WAITING,
    /**
     * 正在停止
     */
    STOPPING,
    /**
     * 已停止
     */
    STOPPED,
    /**
     * 正在运行
     */
    RUNNING,
    /**
     * 成功
     */
    SUCCESS,
    /**
     * 失败
     */
    ERROR;

    /**
     * 任务是否结束
     * 成功/失败/已停止 都认为是结束状态
     *
     * @return
     */
    public boolean isEnd() {
        return this == SUCCESS || this == ERROR || this == STOPPED;
    }

}
