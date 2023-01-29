package com.data.udh.dto;

import lombok.Data;

import java.util.List;
@Data
public class StackConfiguration {
    private String name;
    private String description;
    private String label;
    private String recommendExpression;
    private String valueType;
    private boolean configurableInWizard;
    private List<String> groups;
}
