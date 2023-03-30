package com.data.udh.entity;

import com.data.udh.enums.ServiceRoleState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RoleStateConverter implements AttributeConverter<ServiceRoleState, Integer> {


    @Override
    public Integer convertToDatabaseColumn(ServiceRoleState roleState) {
        return roleState.getValue();
    }

    @Override
    public ServiceRoleState convertToEntityAttribute(Integer integer) {
        for (ServiceRoleState serviceState : ServiceRoleState.values()) {
            if (serviceState.getValue() == integer) {
                return serviceState;
            }
        }
        return null;
    }
}