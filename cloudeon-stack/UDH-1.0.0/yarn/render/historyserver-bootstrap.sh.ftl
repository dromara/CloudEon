#!/usr/bin/env bash





echo "========================start historyserver========================"
${r"${HADOOP_HOME}"}/sbin/mr-jobhistory-daemon.sh --config /opt/udh/${service.serviceName}/conf start historyserver
tail -f /dev/null