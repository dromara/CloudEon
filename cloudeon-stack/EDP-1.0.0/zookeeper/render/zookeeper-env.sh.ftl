#!/usr/bin/env bash

export ZOOKEEPER_LOG_DIR=/opt/edp/${service.serviceName}/log
export ZOOKEEPER_DATA_DIR=/opt/edp/${service.serviceName}/data
export ZOOPIDFILE="/opt/edp/${service.serviceName}/data/zookeeper-server.pid"

export SERVER_JVMFLAGS="-Dcom.sun.management.jmxremote.port=${conf['zookeeper.jmxremote.port']} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false"

<#assign ramPercentage = conf['zookeeper.jvm.memory.percentage']?number>
# 优先使用zk内置的环境变量，用于设置Xmx，单位为m
export ZK_SERVER_HEAP=$[ $MEM_LIMIT / 1024 / 1024  * ${ramPercentage} / 100 ]

export SERVER_JVMFLAGS="-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/opt/edp/${service.serviceName}/log/gc-zookeeper-server.log   -Dzookeeper.log.dir=/opt/edp/${service.serviceName}/log -Dzookeeper.root.logger=INFO,ROLLINGFILE $SERVER_JVMFLAGS"

export SERVER_JVMFLAGS="-Dznode.container.checkIntervalMs=${conf['znode.container.checkIntervalMs']} $SERVER_JVMFLAGS"

export SERVER_JVMFLAGS=" -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent-0.14.0.jar=5541:/opt/edp/${service.serviceName}/conf/jmx_zookeeper.yaml $SERVER_JVMFLAGS"
