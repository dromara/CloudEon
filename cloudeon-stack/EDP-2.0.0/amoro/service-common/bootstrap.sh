#!/usr/bin/env bash
set -e

\cp -f /opt/service-render-output/* $AMORO_HOME/conf/

mv $AMORO_HOME/conf/flink-conf.yaml $FLINK_HOME/conf/

\cp -f /etc/hdfs-config/* $HADOOP_CONF_DIR
\cp -f /etc/yarn-config/* $HADOOP_CONF_DIR

mkdir -p /workspace/logs
rm -rf $AMORO_HOME/logs
ln -s /workspace/logs $AMORO_HOME/logs

ams.sh start

until find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' | grep -q .
do
  echo "`date`: Waiting for logs..."
  sleep 2
done
find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' -exec tail -F {} +

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null



