package com.data.udh.utils;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum TaskGroupType {

    PULL_IMAGE_FROM_REGISTRY(1, "从dockerRegistry拉取镜像", Lists.newArrayList(TaskType.PULL_IMAGE_FROM_REGISTRY_TO_HOST), true),
    INSTALL_SERVICE(2, "安装服务", Lists.newArrayList(TaskType.INSTALL_ROLE_TO_HOST), true),
    CONFIG_SERVICE(3, "配置服务", Lists.newArrayList(TaskType.CONFIG_ROLE_TO_HOST), true),
    TAG_AND_START_K8S_SERVICE(4, "添加标签并启动k8s服务", Lists.newArrayList(TaskType.TAG_HOST, TaskType.START_K8S_SERVICE), true),
    INIT_HDFS(5, "初始化HDFS", Lists.newArrayList(TaskType.INIT_HDFS_NAMENODE), false),
    INIT_YARN(6, "初始化YARN", Lists.newArrayList(TaskType.HDFS_MKDIR), false),
    CANCEL_TAG_AND_STOP_K8S_SERVICE(7, "移除标签并停止k8s服务", Lists.newArrayList(TaskType.CANCEL_TAG_HOST, TaskType.STOP_K8S_SERVICE), true),
    INIT_DORIS(8, "初始化DORIS", Lists.newArrayList(TaskType.REGISTER_BE), false),


    ;

    private final int code;

    private final String name;
    private final List<TaskType> taskTypes;
    private final boolean isRoleLoop;


    public static TaskGroupType of(int code) {
        for (TaskGroupType nodeType : values()) {
            if (nodeType.code == code) {
                return nodeType;
            }
        }
        throw new IllegalArgumentException("unknown TaskGroupType of " + code);
    }

}
