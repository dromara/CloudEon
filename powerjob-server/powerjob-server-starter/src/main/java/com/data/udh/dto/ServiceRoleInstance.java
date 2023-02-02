package com.data.udh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRoleInstance {
    private Integer id;
    private String stackServiceName;
    private String serviceName;
    private LinkedHashMap<String,List<NodeInfo>> roleHostMaps;



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NodeInfo {
        private String ip;
        private String hostName;
    }

}
