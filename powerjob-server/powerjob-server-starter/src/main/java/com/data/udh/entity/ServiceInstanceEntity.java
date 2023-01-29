package com.data.udh.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 服务实例表
 */
@Entity
@Table(name = "udh_service_instance")
@Data
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
     * 服务实例编码
     */
    private String sid;



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
     * 服务状态 1、待安装 2：正在运行  3：存在告警 4:存在异常
     */
    private Integer serviceState;

    /**
     * 是否需要重启
     */
    private boolean needRestart;
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
     * 是否开启Kerberos
     */
    private boolean enableKerberos;


}