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

import org.dromara.cloudeon.enums.ServiceState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 服务实例表
 */
@Entity
@Table(name = "ce_service_instance")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInstanceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;




    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 服务名称
     */
    private String serviceName;

    private String label;
    /**
     * 服务状态
     */
    @Convert(converter = ServiceStateConverter.class)
    private ServiceState serviceState;

    /**
     * 是否需要重启
     */
    private Boolean needRestart;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 该服务实例所依赖的服务实例id
     */
    private String dependenceServiceInstanceIds;
    /**
     * 框架服务id
     */
    private Integer stackServiceId;

    /**
     * 相同框架服务的内部实例序号
     */
    private Integer instanceSequence;

    /**
     * 是否开启Kerberos
     */
    private Boolean enableKerberos;

    private String persistencePaths;



}