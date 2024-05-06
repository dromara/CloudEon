#!/bin/bash
set -e

mkdir -p /workspace/data
mkdir -p /workspace/logs

\cp -f /opt/service-render-output/* $KYUUBI_HOME/conf/
\cp -f /etc/hdfs-config/* $HADOOP_CONF_DIR
\cp -f /etc/yarn-config/* $HADOOP_CONF_DIR
\cp -f $KYUUBI_HOME/conf/hive-site.xml $SPARK_HOME/conf/

kyuubi start

until find /workspace/logs -mmin -1 -type f -name '*.out' ! -name '*gc*' | grep -q .
do
  echo "`date`: Waiting for logs..."
  sleep 2
done
find /workspace/logs -mmin -1 -type f -name '*.out' ! -name '*gc*' -exec tail -F {} +

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null

# 使用 thrift jdbc连接(kyuubi直连)
$KYUUBI_HOME/bin/beeline -u "jdbc:kyuubi://$HOSTNAME:10509/default;user=root"
# 使用 thrift jdbc连接(zookeeper 高可用连接)
$KYUUBI_HOME/bin/beeline -u 'jdbc:hive2://zk节点:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=kyuubi' -n root

# 参考hive的命令进行测试
show tables;
select * from test_table limit 10;
insert into test_table values(95,'95a');
select * from test_table limit 10;

