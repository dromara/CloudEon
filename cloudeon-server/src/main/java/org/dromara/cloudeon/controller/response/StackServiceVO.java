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
package org.dromara.cloudeon.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class StackServiceVO {
    private String label;
    private String name;
    private Integer id;
    private String version;
    private String description;
    private String dockerImage;
    private String iconApp;
    private String iconDanger;
    private boolean supportKerberos;
    private String iconDefault;
    /**
     * 该集群是否已经安装过相同的框架服务
     */
    private boolean installedInCluster;

    /**
     * 角色
     */
    private List<String> roles;




}
