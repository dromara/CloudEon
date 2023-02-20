package com.data.udh.dto;

import com.data.udh.entity.CommandTaskEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ServiceProgress {
    private String currentState;
    private String serviceInstanceName;
    private List<CommandTaskEntity> taskDetails;
}
