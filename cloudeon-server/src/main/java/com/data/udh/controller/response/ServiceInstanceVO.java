package com.data.udh.controller.response;

import lombok.Data;

@Data
public class ServiceInstanceVO {
    private String serviceName;
    private Integer id;
    private String serviceStateValue;
    private String icon;
}
