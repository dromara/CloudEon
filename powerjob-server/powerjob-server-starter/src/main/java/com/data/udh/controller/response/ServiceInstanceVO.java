package com.data.udh.controller.response;

import com.data.udh.utils.ServiceState;
import lombok.Data;

@Data
public class ServiceInstanceVO {
    private String serviceName;
    private Integer id;
    private String serviceStateValue;
    private String icon;
}
