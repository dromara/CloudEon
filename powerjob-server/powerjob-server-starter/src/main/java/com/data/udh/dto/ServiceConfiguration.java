package com.data.udh.dto;

import lombok.Data;

@Data
public class ServiceConfiguration {
    private String name;
    private String description;
    private String label;
    private String recommendExpression;
    private String valueType;
    private String confFile;
    private boolean isCustomConf;
}
