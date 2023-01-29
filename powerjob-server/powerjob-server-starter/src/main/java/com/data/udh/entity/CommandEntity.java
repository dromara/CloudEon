package com.data.udh.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 指令表
 */
@Entity
@Table(name = "udh_command")
@Data
public class CommandEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    /**
     * 指令名称
     */
    private String name;

    /**
     * 指令类型
     */
    private String type;

    /**
     * 关联的powerjob的jobs
     */
    private String pjJobIds;

    /**
     * 关联的powerjob的workflow id
     */
    private Integer pjWorkflowId;

    /**
     * 关联的powerjob的instance id
     */
    private Integer pjInstanceId;
}
