#!/usr/bin/env bash
set -e

mkdir -p /workspace/data
mkdir -p /workspace/logs

\cp -f /etc/hdfs-config/* $HADOOP_CONF_DIR
\cp -f /opt/service-render-output/* $HADOOP_CONF_DIR

if [[ "${ROLE_FULL_NAME}" == "hadoop-yarn-resourcemanager" ]]; then
  echo "========================start resourcemanager========================"
  yarn-daemon.sh start resourcemanager
fi
if [[ "${ROLE_FULL_NAME}" == "hadoop-yarn-nodemanager" ]]; then
  echo "========================start nodemanager========================"
  yarn-daemon.sh start nodemanager
fi
if [[ "${ROLE_FULL_NAME}" == "hadoop-yarn-historyserver" ]]; then
  echo "========================start historyserver========================"
  mr-jobhistory-daemon.sh start historyserver
fi
if [[ "${ROLE_FULL_NAME}" == "hadoop-yarn-timelineserver" ]]; then
echo "========================start timelineserver========================"
  yarn-daemon.sh start timelineserver
fi

until find /workspace/logs -mmin -1 | egrep -q '.*'; echo "`date`: Waiting for logs..." ; do sleep 2 ; done
tail -F /workspace/logs/* &

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null