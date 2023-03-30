#!/bin/bash

nohup hive --service hiveserver2 >> /opt/udh/${service.serviceName}/metastore_log_`date '+%Y-%m-%d'` 2>&1 &

tail -f /dev/null