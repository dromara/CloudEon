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
import org.dromara.cloudeon.enums.QuotaState;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 集群告警指标表 
 * 
 */
@Entity
@Data
@Table(name = "ce_cluster_alert_quota")
public class ClusterAlertQuotaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	/**
	 * 告警指标名称
	 */
	private String alertQuotaName;
	/**
	 * 服务分类
	 */
	private String serviceCategory;
	/**
	 * 告警指标表达式
	 */
	private String alertExpr;
	/**
	 * 告警级别 1:警告2：异常
	 */
	@Convert(converter = AlertLevelConverter.class)
	private AlertLevel alertLevel;
	/**
	 * 告警组
	 */
	private Integer alertGroupId;
	/**
	 * 通知组
	 */
	private Integer noticeGroupId;
	/**
	 * 告警建议
	 */
	private String alertAdvice;
	/**
	 * 比较方式 !=;>;<
	 */
	private String compareMethod;
	/**
	 * 告警阀值
	 */
	private Long alertThreshold;
	/**
	 * 告警策略 1:单次2：连续
	 */
	private Integer alertTactic;
	/**
	 * 间隔时长 单位分钟
	 */
	private Integer intervalDuration;
	/**
	 * 触发时长 单位秒
	 */
	private Integer triggerDuration;

	private String serviceRoleName;

	@Convert(converter = QuotaStateConverter.class)
	private QuotaState quotaState;

	private Date createTime;


}
