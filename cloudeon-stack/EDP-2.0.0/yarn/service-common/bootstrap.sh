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

until find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' | grep -q .
do
  echo "`date`: Waiting for logs..."
  sleep 2
done
find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' -exec tail -F {} +

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null

# 功能测试，不会自动执行
yarn jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar  pi 16 10000

hadoop fs -mkdir /tmp
hadoop fs -put README.txt  /tmp/
hadoop fs -rm -r /tmp/output
yarn jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar  wordcount /tmp/README.txt /tmp/output
hadoop fs -ls /tmp/output
hadoop fs -cat /tmp/output/part-r-00000
hadoop fs -rm -r /tmp/output
hadoop fs -rm -r /tmp/README.txt
