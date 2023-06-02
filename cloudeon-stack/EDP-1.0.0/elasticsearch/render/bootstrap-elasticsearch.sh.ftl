#!/bin/bash

export ES_PATH_CONF="/opt/edp/${service.serviceName}/conf"

elasticsearch -d
nohup /home/hadoop/elasticsearch_exporter/elasticsearch_exporter --es.all --es.indices --es.cluster_settings --es.node="test" --es.indices_settings --es.shards --es.snapshots --es.timeout=5s --web.listen-address ":${conf['elasticsearch.exporter.port']}" --web.telemetry-path "/metrics" --es.ssl-skip-verify --es.clusterinfo.interval=5m --es.uri http://${localhostname}:${conf['elasticsearch.http.listeners.port']} > /opt/edp/${service.serviceName}/log/elasticsearch_exporter.log 2>&1 &

tail -f /dev/null