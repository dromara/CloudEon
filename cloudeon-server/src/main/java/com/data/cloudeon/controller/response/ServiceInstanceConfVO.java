package com.data.cloudeon.controller.response;

import com.data.cloudeon.dto.ServiceConfiguration;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ServiceInstanceConfVO {
    private List<ServiceConfiguration> confs;
    private List<String> customFileNames;
    Map<String, List<String>> fileGroupMap = new HashMap<>();


}
