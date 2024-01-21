package org.dromara.cloudeon.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.Getter;

@Getter
public enum RoleType {

    EMPTY(false),
    JOB(true),
    HELM_CHART(false),
    DEPLOYMENT(true),
    UNKNOWN(false);

    private boolean supportLogs;

    RoleType(boolean supportLogs) {
        this.supportLogs = supportLogs;
    }

    public static RoleType getRoleType(String type) {
        return EnumUtil.fromString(RoleType.class, type, RoleType.UNKNOWN);
    }

}
