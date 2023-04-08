#!/bin/bash



hbase-daemon.sh --config /opt/udh/${service.serviceName}/conf start regionserver


tail -f /dev/null