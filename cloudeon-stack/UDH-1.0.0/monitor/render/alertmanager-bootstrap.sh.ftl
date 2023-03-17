#!/usr/bin/env bash





echo "========================start nodemanager========================"
${r"${HADOOP_HOME}"}/sbin/yarn-daemon.sh --config /opt/udh/${service.serviceName}/conf start nodemanager

tail -f /dev/null