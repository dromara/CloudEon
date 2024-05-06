#!/bin/bash
set -e

mkdir -p /workspace/logs

\cp -f /opt/service-render-output/* $KAFKA_HOME/config/

source $KAFKA_HOME/config/kafka-env.sh
kafka-server-start.sh  $KAFKA_HOME/config/server.properties

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null

# 功能测试，不会自动执行

kafka-topics.sh --zookeeper $HOSTNAME:$ZK_CLIENT_PORT/kafka --create --topic t1 --partitions 1 --replication-factor 1
kafka-topics.sh --zookeeper $HOSTNAME:$ZK_CLIENT_PORT/kafka --list

kafka-console-producer.sh --bootstrap-server $HOSTNAME:$KAFKA_PORT --topic t1
kafka-console-consumer.sh --bootstrap-server $HOSTNAME:$KAFKA_PORT --from-beginning  --topic t1