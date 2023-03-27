package com.data.udh.controller.response;

import lombok.Data;

@Data
public class ServiceInstanceVO {
    private String serviceName;
    private Integer id;
    private String serviceState;
    private Integer serviceStateValue;
    private String icon;
}
