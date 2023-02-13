package com.data.udh.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskType {

    TAG_HOST(1, "添加k8s节点标签", "com.data.udh.processor.ExampleTask", true),
    START_K8S_SERVICE(2, "启动k8s服务", "com.data.udh.processor.ExampleTask", false),
    PULL_IMAGE_FROM_REGISTRY_TO_HOST(3, "拉取docker registry镜像到节点", "com.data.udh.processor.ExampleTask", true),
    INSTALL_ROLE_TO_HOST(4, "安装服务角色到节点", "com.data.udh.processor.ExampleTask", true),
    CONFIG_ROLE_TO_HOST(5, "配置服务角色到节点", "com.data.udh.processor.ExampleTask", true),
    INIT_HDFS_NAMENODE(6, "初始化NameNode", "com.data.udh.processor.ExampleTask", false),
    HDFS_MKDIR(7, "HDFS上创建目录", "com.data.udh.processor.ExampleTask", false),
    CANCEL_TAG_HOST(8, "移除节点上的标签", "com.data.udh.processor.ExampleTask", true),
    STOP_K8S_SERVICE(9, "停止K8s服务", "com.data.udh.processor.ExampleTask", false),


    ;

    private final int code;

    private final String name;

    /**
     * 处理器类
     */
    private final String processorClass;

    private final boolean isHostLoop;

    public static TaskType of(int code) {
        for (TaskType nodeType : values()) {
            if (nodeType.code == code) {
                return nodeType;
            }
        }
        throw new IllegalArgumentException("unknown TaskGroupType of " + code);
    }

}