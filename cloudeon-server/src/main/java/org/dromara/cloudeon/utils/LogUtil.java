package org.dromara.cloudeon.utils;

import org.dromara.cloudeon.processor.TaskParam;
import org.slf4j.MDC;

public enum LogUtil {
    /**
     * 单例
     */
    INSTANCE;

    /**
     * 使用此方法来确保log taskId信息正确，避免因复杂多线程环境导致的日志记录错误
     *
     * @param taskParam
     * @param logFunction
     */
    public static void logWithTaskId(TaskParam taskParam, LogFunction logFunction) {
        if (taskParam == null) {
            logFunction.log();
        } else {
            MDC.put(Constant.TASK_ID, (taskParam.getCommandId() + "-" + taskParam.getCommandTaskId()));
            logFunction.log();
            MDC.clear();
        }
    }

    public interface LogFunction {
        void log();
    }
}
