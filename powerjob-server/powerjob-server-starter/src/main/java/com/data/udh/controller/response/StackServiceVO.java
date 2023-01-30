package com.data.udh.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class StackServiceVO {
    private String label;
    private String name;
    private Integer id;
    private String version;
    private String description;
    private String dockerImage;
    private String iconApp;
    private String iconDanger;
    private String iconDefault;

    /**
     * 角色
     */
    private List<String> roles;




}
