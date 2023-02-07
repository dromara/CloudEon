package com.data.udh.entity;

import com.data.udh.utils.CommandState;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 指令任务表
 */
@Entity
@Table(name = "udh_command_task")
@Data
public class CommandTaskEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    /**
     * 任务展示排序
     */
    private Integer taskShowSortNum;

    /**
     * 任务名称
     */
    private String taskName;

    private String processorClassName;

    /**
     * 运行状态
     */
    @Enumerated(EnumType.STRING)
    private CommandState commandState;


    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;


    private Integer commandTaskGroupId;

    private Integer commandId;

    private Integer serviceInstanceId;


    /**
     * 进度
     */
    private Integer progress;

    private Long powerjobInstanceId;

}



