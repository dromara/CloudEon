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
 * 框架服务角色表
 * 
 */
@Data
@Entity
@Table(name = "ce_stack_service_role")
public class StackServiceRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	/**
	 * 服务id
	 */
	private Integer serviceId;
	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 前端显示时的名字
	 */
	private String label;

	private String roleFullName;

	/**
	 * 角色ui页面链接模板
	 */
	private String linkExpression;
	/**
	 * 角色类型 master / slave
	 */
	private String type;


	private Integer sortNum;


	private Integer stackId;


	private String jmxPort;

	/**
	 * 角色分配建议配置
	 */
	private String assign;

	/**
	 * 角色支持的操作
	 */
	private String frontendOperations;

	private Integer minNum;
	private Integer fixedNum;
	private boolean needOdd;

	private String logFile;

}
