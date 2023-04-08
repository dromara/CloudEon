package com.data.cloudeon.entity;

import com.data.cloudeon.enums.QuotaState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class QuotaStateConverter implements AttributeConverter<QuotaState, Integer> {


    @Override
    public Integer convertToDatabaseColumn(QuotaState quotaState) {
        return quotaState.getValue();
    }

    @Override
    public QuotaState convertToEntityAttribute(Integer integer) {
        for (QuotaState quotaState : QuotaState.values()) {
            if (quotaState.getValue() == integer) {
                return quotaState;
            }
        }
        return null;
    }
}