#!/usr/bin/env bash
set -e

\cp -f /opt/service-render-output/* $HBASE_HOME/conf/
\cp -f /etc/hdfs-config/* $HADOOP_CONF_DIR

mkdir -p /workspace/logs

if [[ "${ROLE_FULL_NAME}" == "hbase-master" ]]; then
hbase-daemon.sh start master
fi
if [[ "${ROLE_FULL_NAME}" == "hbase-regionserver" ]]; then
hbase-daemon.sh start regionserver
fi

until find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' | grep -q .
do
  echo "`date`: Waiting for logs..."
  sleep 2
done
find /workspace/logs -mmin -1 -type f -name '*.log' ! -name '*gc*' -exec tail -F {} +

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null

# 进入shell执行后续命令
hbase shell
# 查看集群状态
status
# 查看所有表
list
# 创建表
create 'user','id','address','info'
# 查看表
describe 'user'
# 插入数据
put 'user', 'u1','id','i1'
put 'user', 'u1','address','cn'
put 'user', 'u1','info','xyz'
# 扫描表
scan 'user'