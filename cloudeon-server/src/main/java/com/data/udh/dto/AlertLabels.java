package com.data.udh.dto;

import lombok.Data;

@Data
public class AlertLabels {

    private String alertname;
    private int clusterId;
    private String serviceRoleName;
    private String instance;
    private String job;
    private String severity;

}