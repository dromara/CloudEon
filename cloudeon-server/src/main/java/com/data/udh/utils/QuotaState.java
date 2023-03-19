package com.data.udh.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QuotaState {
    RUNNING(1,"启用"),
    STOPPED(2,"未启用");

    private int value;

    private String desc;

    QuotaState(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @JsonValue
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return this.desc;
    }
}
