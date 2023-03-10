#!/usr/bin/env bash



source /opt/udh/${service.serviceName}/conf/hadoop-hdfs-env.sh


echo "========================start zkfc========================"
${r"${HADOOP_HOME}"}/sbin/hadoop-daemon.sh --config /opt/udh/${service.serviceName}/conf start zkfc
echo "========================start namenode========================"
${r"${HADOOP_HOME}"}/sbin/hadoop-daemon.sh --config /opt/udh/${service.serviceName}/conf start namenode

tail -f /dev/null