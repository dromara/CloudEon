#!/usr/bin/env bash

source /opt/udh/${service.serviceName}/conf/hadoop-hdfs-env.sh

echo "========================starting journalnode========================"
${r"${HADOOP_HOME}"}/sbin/hadoop-daemon.sh --config /opt/udh/${service.serviceName}/conf start journalnode

tail -f /dev/null