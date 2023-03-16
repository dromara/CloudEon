package com.data.udh.dto;

import lombok.Data;

import java.util.List;

@Data
public class StackServiceInfo {

    private String name;
    private String label;
    private String description;
    private Boolean supportKerberos;
    private String version;
    private String dockerImage;
    private String runAs;
    private List<String> dependencies;
    private List<String> customConfigFiles;
    private List<StackServiceRole> roles;
    private List<StackConfiguration> configurations;
    private List<String> persistencePaths;
}
