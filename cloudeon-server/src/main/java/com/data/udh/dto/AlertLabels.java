package com.data.udh.dto;

import lombok.Data;

@Data
public class AlertLabels {


    private String alertname;
    private String instance;
    private String job;


    private int clusterId;
    private String serviceRoleName;
    /**
     * exception(s1) 或 warning(s2)
     */
    private String alertLevel;

}