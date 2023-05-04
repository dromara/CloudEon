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
package org.dromara.cloudeon.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 集群框架版本服务表
 *
 */
@Data
@Entity
@Table(name = "ce_stack_service")
public class StackServiceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")    private Integer id;
    /**
     * 框架id
     */
    private Integer stackId;

    private String stackCode;

    /**
     * 服务名称
     */
    private String name;

    private String runAs;

    /**
     * 服务版本
     */
    private String version;
    private String dashboardUid;
    /**
     * 服务描述
     */
    private String description;
    private String label;

    private String dockerImage;

    private boolean supportKerberos;

    private String dependencies;


    private String serviceConfigurationYaml;

    private String serviceConfigurationYamlMd5;

    private String serviceRoleYaml;

    private String serviceRoleYamlMd5;

    /**
     * 详情页面支持哪些tab
     */
    private String pages;

    /**
     * 支持的自定义配置文件
     */
    private String customConfigFiles;


    private Integer sortNum;

    private String iconApp;
    private String iconDanger;
    private String iconDefault;
    private String persistencePaths;
   

}
