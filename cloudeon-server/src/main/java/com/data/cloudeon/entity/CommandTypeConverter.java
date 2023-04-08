package com.data.cloudeon.entity;

import com.data.cloudeon.enums.CommandType;

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