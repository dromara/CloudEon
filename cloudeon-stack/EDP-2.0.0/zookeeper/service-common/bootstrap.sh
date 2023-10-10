#!/bin/bash
set -e

mkdir -p /workspace/data
mkdir -p /workspace/logs

\cp -f /opt/service-render-output/myid /workspace/data/
\cp -f /opt/service-render-output/zoo.cfg $ZOOKEEPER_HOME/conf/
\cp -f /opt/service-render-output/zookeeper-env.sh $ZOOKEEPER_HOME/conf/

$ZOOKEEPER_HOME/bin/zkServer.sh start

sleep 5

until find /workspace/logs -mmin -1 | egrep -q '.*'; echo "`date`: Waiting for logs..." ; do sleep 2 ; done
tail -F /workspace/logs/* &

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null