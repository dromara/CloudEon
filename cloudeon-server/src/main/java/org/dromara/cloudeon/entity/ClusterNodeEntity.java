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
import org.dromara.cloudeon.enums.SshAuthType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 集群主机表
 *
 */
@Entity
@Table(name = "ce_cluster_node")
@Data
public class ClusterNodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 主机名
     */
    private String hostname;
    /**
     * IP
     */
    private String ip;
    /**
     * 机架
     */
    private String rack;
    private String runtimeContainer;
    /**
     * 核数
     */
    private Integer coreNum;
    /**
     * 总内存
     */
    private Integer totalMem;
    /**
     * 总磁盘
     */
    private Integer totalDisk;

    private String sshUser;
    private String sshPassword;
    private Integer sshPort;

    @Enumerated(EnumType.STRING)
    private SshAuthType sshAuthType;

    private String privateKeyPath;

    /**
     * 集群id
     */
    private Integer clusterId;


    private String cpuArchitecture;

    private String nodeLabel;

    private Integer serviceRoleNum;

}
