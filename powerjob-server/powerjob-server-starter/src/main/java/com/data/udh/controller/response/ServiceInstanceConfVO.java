package com.data.udh.controller.response;

import com.data.udh.dto.ServiceConfiguration;
import com.data.udh.dto.StackConfiguration;
import lombok.Data;

import java.util.List;

@Data
public class ServiceInstanceConfVO {
    private List<ServiceConfiguration> confs;
    private List<String> customFileNames;

}
