package com.data.udh.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 框架服务配置表
 * 
 */
@Data
@Entity
@Table(name = "udh_stack_service_conf")
public class StackServiceConfEntity implements Serializable {
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


	private Integer stackId;

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 前端显示时的名字
	 */
	private String label;

	private String description;

	private String recommendExpression;


	private String confFile;

	private Boolean configurableInWizard;

	private String valueType;


}
