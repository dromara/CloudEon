package com.data.cloudeon.controller.response;

import com.data.cloudeon.dto.ServiceConfiguration;
import lombok.Data;

import java.util.List;

@Data
public class ServiceInstanceConfVO {
    private List<ServiceConfiguration> confs;
    private List<String> customFileNames;

}
