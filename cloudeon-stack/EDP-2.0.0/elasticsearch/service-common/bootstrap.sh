#!/bin/bash

chown elasticsearch:elasticsearch /workspace
mkdir /data
chown elasticsearch:elasticsearch /data

su -m elasticsearch -c "mkdir -p /workspace/data"
su -m elasticsearch -c "mkdir -p /workspace/logs"
su -m elasticsearch -c "mkdir -p /workspace/tmp"

\cp -f /opt/service-render-output/elasticsearch.yml $ES_HOME/config/
\cp -f /opt/service-render-output/jvm.options $ES_HOME/config/jvm.options.d/

su -m elasticsearch -c "elasticsearch -d -p /workspace/elasticsearch-pid"

sleep 5

until find /workspace/logs -mmin -1 | egrep -q '.*'; echo "`date`: Waiting for logs..." ; do sleep 2 ; done

find /workspace/logs -type f -name '*.log' ! -name '*gc*' -exec tail -F {} +

echo "---------------------------------------------开始----------------------------------------------"
tail -f /dev/null
# 查看集群状态
curl -XGET "http://${HOSTNAME}:${ES_HTTP_PORT}/_cluster/health?pretty"
