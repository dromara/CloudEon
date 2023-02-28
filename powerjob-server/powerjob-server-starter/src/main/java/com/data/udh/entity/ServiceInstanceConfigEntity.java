package com.data.udh.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 服务实例配置表
 */
@Entity
@Table(name = "udh_service_instance_config")
@Data
public class ServiceInstanceConfigEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    /**
     * 服务实例id
     */
    private Integer serviceInstanceId;

    /**
     * 配置项
     */
    private String name;


    private Integer nodeId;

    /**
     * 值
     */
    private String value;

    /**
     * 所属配置文件
     */
    private String confFile;

    /**
     * 是否属于自定义配置
     */
    private boolean isCustomConf;

    /**
     * 框架推荐值
     */
    private String recommendedValue;


    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;


    private Integer userId;

    private Integer min;
    private Integer max;
    /**
     * 单位
     */
    private String unit;
    /**
     * 是否密码
     */
    private boolean isPassword;
    /**
     * 是否多值输入。像多了路径：/hdfs/path1,/hdfs/path2
     */
    private boolean isMultiValue;
    /**
     * 下拉框的选项值，逗号分隔
     */
    private String options;

}