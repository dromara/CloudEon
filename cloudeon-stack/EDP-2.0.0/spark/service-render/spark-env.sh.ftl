#!/usr/bin/env bash

<#if conf['plugin.iceberg'] !='true'>
rm -f $SPARK_HOME/jars/iceberg-spark-runtime-*.jar
</#if>


<#assign ramPercentage = conf['spark.daemon.jvm.memory.percentage']?number>

export SPARK_DAEMON_MEMORY=$[ $MEM_LIMIT / 1024 / 1024  * ${ramPercentage} / 100 ]M

export SPARK_LOG_DIR=/workspace/logs
export SPARK_PID_DIR=/workspace/data
export SPARK_CONF_DIR=$SPARK_HOME/conf
<#if conf["data.path.list"]??&& conf["data.path.list"]?trim?has_content>
    <#assign dataPathListSize=conf["data.path.list"]?trim?split(",")?size>
<#else >
    <#assign dataPathListSize=1>
</#if>
<#assign concatenatedPaths="">
<#list 1..dataPathListSize as dataPathIndex>
    <#assign concatenatedPaths = concatenatedPaths + "/data/${dataPathIndex}">
    <#if dataPathIndex < dataPathListSize>
        <#assign concatenatedPaths = concatenatedPaths + ",">
    </#if>
</#list>
export SPARK_LOCAL_DIRS=${concatenatedPaths}


export SPARK_HISTORY_OPTS="$SPARK_HISTORY_OPTS -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/workspace/logs/gc-spark-historyserver.log -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=9924 -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent.jar=5554:$SPARK_HOME/conf/jmx_prometheus.yaml"
