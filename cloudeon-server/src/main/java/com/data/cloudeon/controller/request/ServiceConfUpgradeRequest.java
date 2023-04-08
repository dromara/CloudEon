package com.data.cloudeon.controller.request;

import com.data.cloudeon.dto.ServiceCustomConf;
import com.data.cloudeon.dto.ServicePresetConf;
import lombok.Data;

import java.util.List;

@Data
public class ServiceConfUpgradeRequest {
    private List<ServicePresetConf> presetConfList;
    private List<ServiceCustomConf> customConfList;
    private Integer serviceInstanceId;


}
