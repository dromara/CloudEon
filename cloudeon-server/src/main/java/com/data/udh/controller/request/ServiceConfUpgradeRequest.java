package com.data.udh.controller.request;

import com.data.udh.dto.ServiceCustomConf;
import com.data.udh.dto.ServicePresetConf;
import lombok.Data;

import java.util.List;

@Data
public class ServiceConfUpgradeRequest {
    private List<ServicePresetConf> presetConfList;
    private List<ServiceCustomConf> customConfList;
    private Integer serviceInstanceId;


}
