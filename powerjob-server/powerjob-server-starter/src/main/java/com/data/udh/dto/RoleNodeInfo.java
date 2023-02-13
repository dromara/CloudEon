package com.data.udh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleNodeInfo {
    private Integer id;
    private String hostname;
    private String roleName;

}
