#!/usr/bin/env bash
set -e

mkdir -p /workspace/data
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

until find /workspace/logs -mmin -1 | egrep -q '.*'; echo "`date`: Waiting for logs..." ; do sleep 2 ; done
tail -F /workspace/logs/* &

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null