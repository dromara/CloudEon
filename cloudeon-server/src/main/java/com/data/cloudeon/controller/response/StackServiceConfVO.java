package com.data.cloudeon.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class StackServiceConfVO {
    private List<ServiceConfVO> confs;
    private List<String> customFileNames;

}
