#!/bin/bash

set -xeuo pipefail

ETC_DIR=/opt/edp/${service.serviceName}/conf

/home/hadoop/trino/bin/launcher --etc-dir=$ETC_DIR  \
--node-config=$ETC_DIR/worker-node.properties  \
--jvm-config=$ETC_DIR/worker-jvm.config \
--config=$ETC_DIR/worker-config.properties \
--log-levels-file=$ETC_DIR/worker-log.properties \
--server-log-file=/opt/edp/${service.serviceName}/log/worker-server.log  \
--launcher-log-file=/opt/edp/${service.serviceName}/log/worker-launcher.log  \
start

tail -f /dev/null
