package com.data.udh.controller.response;

import lombok.Data;

import java.util.Date;

@Data
public class NodeInfoVO {
    private Integer id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 主机名
     */
    private String hostname;
    /**
     * IP
     */
    private String ip;
    /**
     * 机架
     */
    private String rack;
    /**
     * 核数
     */
    private Integer coreNum;
    /**
     * 总内存
     */
    private Integer totalMem;
    /**
     * 总磁盘
     */
    private Integer totalDisk;

    private String sshUser;
    private Integer sshPort;

    /**
     * 集群id
     */
    private Integer clusterId;


    private String cpuArchitecture;

    private String nodeLabel;

    private Integer serviceRoleNum;
}
