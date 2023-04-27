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
import java.util.Date;

/**
 * 服务实例配置表
 */
@Entity
@Table(name = "ce_service_instance_config")
@Data
public class ServiceInstanceConfigEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    /**
     * 服务实例id
     */
    private Integer serviceInstanceId;

    /**
     * 配置项
     */
    private String name;


    private Integer nodeId;

    /**
     * 值
     */
    private String value;

    /**
     * 所属配置文件
     */
    private String confFile;
    private String tag;

    /**
     * 是否属于自定义配置
     */
    private boolean isCustomConf;

    /**
     * 框架推荐值
     */
    private String recommendedValue;


    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;


    private Integer userId;



}