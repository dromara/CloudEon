#!/usr/bin/env bash





echo "========================start timelineserver========================"
${r"${HADOOP_HOME}"}/sbin/yarn-daemon.sh --config /opt/udh/${service.serviceName}/conf start timelineserver

tail -f /dev/null