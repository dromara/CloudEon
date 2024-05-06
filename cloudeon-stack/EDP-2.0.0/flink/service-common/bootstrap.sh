#!/bin/bash
set -e

mkdir -p /workspace/data
mkdir -p /workspace/logs

\cp -f /opt/service-render-output/* $FLINK_HOME/conf/
\cp -f /etc/hdfs-config/* $HADOOP_CONF_DIR
\cp -f /etc/yarn-config/* $HADOOP_CONF_DIR

export FLINK_ENV_JAVA_OPTS_HS="$FLINK_ENV_JAVA_OPTS_HS  -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=9925 -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent.jar=5555:$FLINK_HOME/conf/jmx_prometheus.yaml"
export HADOOP_CLASSPATH=$(hadoop classpath)

historyserver.sh start


until find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' | grep -q .
do
  echo "`date`: Waiting for logs..."
  sleep 2
done
find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' -exec tail -F {} +

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null

# 功能测试，不会自动执行
export HADOOP_CLASSPATH=`hadoop classpath`
flink run-application -t yarn-application \
${FLINK_HOME}/examples/batch/WordCount.jar
