#!/bin/bash

export HIVE_CONF_DIR=/opt/udh/${service.serviceName}/conf

nohup hive --service metastore >> /opt/udh/${service.serviceName}/metastore_log_`date '+%Y-%m-%d'` 2>&1 &

tail -f /dev/null