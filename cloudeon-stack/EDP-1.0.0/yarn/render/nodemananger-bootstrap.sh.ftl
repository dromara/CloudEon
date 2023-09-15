#!/usr/bin/env bash



cp -f /opt/edp/yarn/conf/yarn-site.xml $HADOOP_HOME/etc/hadoop/

echo "========================start nodemanager========================"
${r"${HADOOP_HOME}"}/sbin/yarn-daemon.sh --config /opt/edp/${service.serviceName}/conf start nodemanager

tail -f /dev/null