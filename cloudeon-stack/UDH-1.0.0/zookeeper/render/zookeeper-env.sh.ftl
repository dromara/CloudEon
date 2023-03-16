#!/usr/bin/env bash

export ZOOKEEPER_LOG_DIR=/opt/udh/${service.serviceName}/log
export ZOOKEEPER_DATA_DIR=/opt/udh/${service.serviceName}/data
export ZOOPIDFILE="/opt/udh/${service.serviceName}/data/zookeeper-server.pid"

export SERVER_JVMFLAGS="-Dcom.sun.management.jmxremote.port=${conf['zookeeper.jmxremote.port']} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false"

<#if conf['zookeeper.container.limits.memory'] != "-1" && conf['zookeeper.memory.ratio'] != "-1">
  <#assign limitsMemory = conf['zookeeper.container.limits.memory']?number
    memoryRatio = conf['zookeeper.memory.ratio']?number
    memory = limitsMemory * memoryRatio * 1024>
<#else>
  <#assign memory = conf['zookeeper.server.memory']?number>
</#if>
export SERVER_JVMFLAGS="-Xmx${memory?floor?c}m $SERVER_JVMFLAGS"

export SERVER_JVMFLAGS="-Dzookeeper.log.dir=/opt/udh/${service.serviceName}/log -Dzookeeper.root.logger=INFO,ROLLINGFILE $SERVER_JVMFLAGS"

export SERVER_JVMFLAGS="-Dznode.container.checkIntervalMs=${conf['znode.container.checkIntervalMs']} $SERVER_JVMFLAGS"


export JAVAAGENT_OPTS=" -javaagent:/usr/lib/jmx_exporter/jmx_prometheus_javaagent-0.7.jar=5556:/usr/lib/jmx_exporter/agentconfig.yml "
