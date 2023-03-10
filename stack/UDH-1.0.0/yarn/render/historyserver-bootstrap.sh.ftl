#!/usr/bin/env bash





echo "========================start historyserver========================"
${r"${HADOOP_HOME}"}/sbin/yarn-daemon.sh --config /opt/udh/${service.serviceName}/conf start historyserver
tail -f /dev/null