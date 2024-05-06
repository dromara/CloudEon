#!/usr/bin/env bash
set -e

\cp -f /opt/service-render-output/* $HIVE_HOME/conf/
\cp -f /etc/hdfs-config/* $HADOOP_CONF_DIR
\cp -f /etc/yarn-config/* $HADOOP_CONF_DIR
mkdir -p /workspace/logs

if [[ "${ROLE_FULL_NAME}" == "hive-init" ]]; then
    echo "========================start hive init========================"
    source $HIVE_HOME/conf/init-warehouse-dir.sh
    source $HIVE_HOME/conf/init-metastore-db.sh
    exit 0
fi

if [[ "${ROLE_FULL_NAME}" == "hive-metastore" ]]; then
    echo "========================start hive metastore========================"
    nohup hive --service metastore >> /workspace/logs/metastore_`date '+%Y-%m-%d'`.log 2>&1 &
fi
if [[ "${ROLE_FULL_NAME}" == "hive-server2" ]]; then
    echo "========================start hive server2========================"
    nohup hive --service hiveserver2 >> /workspace/logs/server2_`date '+%Y-%m-%d'`.log 2>&1 &
fi

until find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' | grep -q .
do
  echo "`date`: Waiting for logs..."
  sleep 2
done
find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' -exec tail -F {} +

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null

# 使用hive shell连接
hive shell
# 使用 thrift jdbc连接
beeline --verbose=true -u "jdbc:hive2://$HOSTNAME:${SERVER2_THRIFT_PORT}/default;"

# 连接后执行测试用的sql
CREATE TABLE IF NOT EXISTS test_table
 (col1 int COMMENT 'Integer Column',
 col2 string COMMENT 'String Column'
 )
 COMMENT 'This is test table'
 ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ','
 STORED AS TEXTFILE;

insert into test_table values(90,'90a');
insert into test_table values(91,'91a');
insert into test_table values(92,'92a');
insert into test_table values(93,'93a');
insert into test_table values(94,'94a');

show tables;
select * from test_table limit 10;

