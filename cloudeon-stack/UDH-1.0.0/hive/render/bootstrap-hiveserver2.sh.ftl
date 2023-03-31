#!/bin/bash




export HIVE_CONF_DIR=/opt/udh/${service.serviceName}/conf

nohup hive --service hiveserver2 >> /opt/udh/${service.serviceName}/log/metastore_log_`date '+%Y-%m-%d'` 2>&1 &

tail -f /dev/null