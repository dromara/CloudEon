package com.data.udh.entity;

import com.data.udh.utils.CommandState;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

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
     * 指令运行状态
     */
    @Enumerated(EnumType.STRING)
    private CommandState commandState;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 总进度
     */
    private Integer currentProgress;





    /**
     * 操作人id
     */
    private Integer operateUserId;

    /**
     * 工作流实例Id
     */
    private Long powerjobWorkflowInstanceId;

    /**
     * App Id
     */
    private Long powerjobAppId;

    private Integer clusterId;
}
