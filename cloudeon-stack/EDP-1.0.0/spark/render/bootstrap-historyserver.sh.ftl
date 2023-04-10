#!/bin/bash



${r"${SPARK_HOME}"}/sbin/start-history-server.sh --properties-file /opt/edp/${service.serviceName}/conf/spark-defaults.conf

tail -f /dev/null