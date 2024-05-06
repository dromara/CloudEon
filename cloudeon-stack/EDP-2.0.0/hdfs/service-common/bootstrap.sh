#!/usr/bin/env bash
set -e

mkdir -p /workspace/logs

\cp -f /opt/service-render-output/* $HADOOP_CONF_DIR

source /opt/service-render-output/hadoop-hdfs-env.sh

if [[ "${ROLE_FULL_NAME}" == "hadoop-hdfs-zkfc-format" ]]; then
  echo "========================zkfc formatZK========================"
  if [[ $(yes N|hdfs zkfc -formatZK 2>&1) =~ "already exists" ]]; then
    echo "ZK node already exists"
  else
    echo "ZK node not exists"
    hdfs zkfc -formatZK -force -nonInteractive
  fi
  exit 0
fi
if [[ "${ROLE_FULL_NAME}" == "hadoop-hdfs-journalnode" ]]; then
  echo "========================starting journalnode========================"
  hadoop-daemon.sh start journalnode
fi
if [[ "${ROLE_FULL_NAME}" == "hadoop-hdfs-namenode" ]]; then
  source /opt/service-render-output/namenode-format.sh
  echo "========================start namenode========================"
  hadoop-daemon.sh  start namenode
  echo "========================start zkfc========================"
  hadoop-daemon.sh  start zkfc
fi
if [[ "${ROLE_FULL_NAME}" == "hadoop-hdfs-datanode" ]]; then
  echo "========================starting datanode========================"
  hadoop-daemon.sh start datanode
fi
if [[ "${ROLE_FULL_NAME}" == "hadoop-hdfs-httpfs" ]]; then
  echo "========================starting httpfs========================"
  hadoop-daemon.sh  start httpfs
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
hadoop fs -ls /
hadoop fs -mkdir /tmp
hadoop fs -put -f $HADOOP_HOME/README.txt /tmp
hadoop fs -cat /tmp/README.txt
hadoop fs -rm /tmp/README.txt
