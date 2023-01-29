package com.data.udh.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 集群服务角色分组实例表
 *
 */
@Entity
@Table(name = "udh_service_role_group_instance")
@Data
public class ServiceRoleGroupInstanceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;
    /**
     * 角色分组名
     */
    private String name;
    /**
     * 绑定的service实例id
     */
    private Integer serviceId;
    /**
     * 前端输入项label
     */
    private String label;





}