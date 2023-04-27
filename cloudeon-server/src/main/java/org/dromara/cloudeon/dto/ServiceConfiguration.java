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
package org.dromara.cloudeon.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceConfiguration {
    private String name;
    private Integer id;
    private String description;
    private String label;
    private String recommendExpression;
    private String valueType;
    private String confFile;
    private String value;
    private String tag;
    private Boolean isCustomConf;
    private Integer min;
    private Integer max;
    /**
     * 单位
     */
    private String unit;
    /**
     * 是否密码
     */
    private boolean isPassword;
    /**
     * 是否多值输入。像多了路径：/hdfs/path1,/hdfs/path2
     */
    private boolean isMultiValue;
    /**
     * 下拉框的选项值，逗号分隔
     */
    private List<String> options;
}
