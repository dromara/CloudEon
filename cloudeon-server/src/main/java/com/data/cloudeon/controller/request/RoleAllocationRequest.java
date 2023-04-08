package com.data.cloudeon.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleAllocationRequest {
    List<Integer> stackServiceIds;

    Integer clusterId;
}
