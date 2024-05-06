#!/bin/bash
set -e

mkdir -p /workspace/data
mkdir -p /workspace/logs

\cp -f /opt/service-render-output/* $SPARK_HOME/conf/
\cp -f /etc/hdfs-config/* $HADOOP_CONF_DIR
\cp -f /etc/yarn-config/* $HADOOP_CONF_DIR

if [[ "${ROLE_FULL_NAME}" == "spark-init" ]]; then
  echo "========================start init-history-hdfs-dir ========================"
  source $SPARK_HOME/conf/init-history-hdfs-dir.sh
  exit 0
fi

if [[ "${ROLE_FULL_NAME}" == "spark-thriftserver" ]]; then
  echo "========================start thriftserver========================"
  ${SPARK_HOME}/sbin/start-thriftserver.sh  --master yarn --deploy-mode client  --executor-memory 2g --total-executor-cores 1
fi
if [[ "${ROLE_FULL_NAME}" == "spark-historyserver" ]]; then
  echo "========================start historyserver========================"
  ${SPARK_HOME}/sbin/start-history-server.sh
fi

until find /workspace/logs -mmin -1 -type f -name '*.out' ! -name '*gc*' | grep -q .
do
  echo "`date`: Waiting for logs..."
  sleep 2
done
find /workspace/logs -mmin -1 -type f -name '*.out' ! -name '*gc*' -exec tail -F {} +

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null

# 使用 thrift jdbc连接
beeline -u "jdbc:hive2://localhost:10555/default;user=root"

# 参考hive的命令进行测试
show tables;
select * from test_table limit 10;
insert into test_table values(94,'94a');

spark-submit --master yarn \
--deploy-mode cluster \
--executor-memory 1G \
--executor-cores 1 \
--class org.apache.spark.examples.SparkPi \
${SPARK_HOME}/examples/jars/spark-examples*.jar \
1000