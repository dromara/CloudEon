package com.data.udh.controller.request;

import com.data.udh.dto.ServiceCustomConf;
import com.data.udh.dto.ServicePresetConf;
import lombok.Data;

import java.util.List;

@Data
public class InitServiceRequest {

    private List<ServiceInfo> serviceInfos;
    private Integer clusterId;
    private Integer stackId;
    private Boolean enableKerberos;


    @Data
    public static class ServiceInfo{

        private Integer stackServiceId;
        private String stackServiceName;
        private String stackServiceLabel;

        private List<InitServiceRole> roles;

        private List<ServicePresetConf> presetConfList;

        private List<ServiceCustomConf> customConfList;

    }



    @Data
    public static class InitServiceRole {
        private String stackRoleName;
        private List<Integer> nodeIds;

    }



}



