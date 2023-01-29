package com.data.udh.entity;

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
@Table(name = "udh_service_role_instance")
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
     * 服务角色状态 1:正在运行2：存在告警3：存在异常4：需要重启
     */
    private Integer serviceRoleState;

    /**
     * 关联角色分组id
     */
    private Integer roleGroupId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 服务id
     */
    private Integer serviceId;
    /**
     * 角色类型 1:master2:worker3:client
     */
    private Integer roleType;
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




}