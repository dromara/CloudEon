#!/usr/bin/env bash

<#assign ramPercentage = conf['spark.daemon.jvm.memory.percentage']?number>

export SPARK_DAEMON_MEMORY=$[ $MEM_LIMIT / 1024 / 1024  * ${ramPercentage} / 100 ]M

export SPARK_LOG_DIR=/opt/edp/${service.serviceName}/log
export SPARK_PID_DIR=/opt/edp/${service.serviceName}/data
export SPARK_CONF_DIR=/opt/edp/${service.serviceName}/conf
export SPARK_LOCAL_DIRS=/opt/edp/${service.serviceName}/data/local