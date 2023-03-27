package com.data.udh.entity;

import com.data.udh.utils.CommandType;
import com.data.udh.utils.ServiceState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CommandTypeConverter implements AttributeConverter<CommandType, String> {


    @Override
    public String convertToDatabaseColumn(CommandType commandType) {
        return commandType.name();
    }

    @Override
    public CommandType convertToEntityAttribute(String name) {
        return CommandType.valueOf(name);
    }
}