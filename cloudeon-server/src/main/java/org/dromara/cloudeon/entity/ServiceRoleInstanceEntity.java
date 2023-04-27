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

import org.dromara.cloudeon.enums.ServiceRoleState;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 集群服务角色实例表
 *
 */
@Entity
@Table(name = "ce_service_role_instance")
@Data
public class ServiceRoleInstanceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;
    /**
     * 服务角色名称
     */
    private String serviceRoleName;
    /**
     * 主机id
     */
    private Integer nodeId;
    /**
     * 服务角色状态
     */
    @Convert(converter = RoleStateConverter.class)
    private ServiceRoleState serviceRoleState;


    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 框架服务角色id
     */
    private Integer stackServiceRoleId;


    /**
     * 角色类型
     */
    private String roleType;
    /**
     * 集群id
     */
    private Integer clusterId;
    /**
     * 所属服务实例id
     */
    private Integer serviceInstanceId;


    private boolean needRestart;

    private boolean isDecommission;


    public ServiceRoleInstanceEntity() {

    }
}