package com.data.udh.dto;

import lombok.Data;

@Data
public class ServiceCustomConf {

    private String name;
    private String value;
    /**
     * 所属配置文件
     */
    private String confFile;

}