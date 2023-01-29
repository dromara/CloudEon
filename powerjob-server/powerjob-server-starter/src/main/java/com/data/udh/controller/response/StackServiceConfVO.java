package com.data.udh.controller.response;

import com.data.udh.dto.StackConfiguration;
import lombok.Data;

import java.util.List;

@Data
public class StackServiceConfVO {
    private List<StackConfiguration> confs;
    private List<String> customFileNames;

}
