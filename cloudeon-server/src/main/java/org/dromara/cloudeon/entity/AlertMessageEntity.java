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

import org.dromara.cloudeon.enums.AlertLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 告警信息表
 *
 */
@Entity
@Data
@Table(name = "ce_alert_message")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertMessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    private String alertName;

    private String alertGroupName;

    private String alertInfo;

    private String alertAdvice;

    private String alertLabels;
    private String hostname;
    private Integer nodeId;
    @Convert(converter = AlertLevelConverter.class)
    private AlertLevel alertLevel;
    private boolean resolved;
    private Integer serviceRoleInstanceId;
    private Integer serviceInstanceId;
    private String fireTime;
    private String solveTime;
    private Integer clusterId;
    private Date createTime;
    private Date updateTime;






}
