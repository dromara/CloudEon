#!/usr/bin/env bash

export ZOO_LOG_DIR=/workspace/logs
export ZOOPIDFILE=/workspace/zookeeper-server.pid

export SERVER_JVMFLAGS="-Dcom.sun.management.jmxremote.port=${conf['zookeeper.jmxremote.port']} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false"

<#assign ramPercentage = conf['zookeeper.jvm.memory.percentage']?number>
# 优先使用zk内置的环境变量，用于设置Xmx，单位为m
export ZK_SERVER_HEAP=$[ $MEM_LIMIT / 1024 / 1024  * ${ramPercentage} / 100 ]

export SERVER_JVMFLAGS="-Dzookeeper.root.logger=INFO,ROLLINGFILE $SERVER_JVMFLAGS"

