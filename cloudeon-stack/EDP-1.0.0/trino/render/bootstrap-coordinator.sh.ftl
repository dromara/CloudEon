#!/bin/bash

set -xeuo pipefail

ETC_DIR=/opt/edp/${service.serviceName}/conf

/home/hadoop/trino/bin/launcher --etc-dir=$ETC_DIR  \
--node-config=$ETC_DIR/coordinator-node.properties  \
--jvm-config=$ETC_DIR/coordinator-jvm.config \
--config=$ETC_DIR/coordinator-config.properties \
--log-levels-file=$ETC_DIR/coordinator-log.properties \
--server-log-file=/opt/edp/${service.serviceName}/log/coordinator-server.log  \
--launcher-log-file=/opt/edp/${service.serviceName}/log/coordinator-launcher.log  \
start

tail -f /dev/null
