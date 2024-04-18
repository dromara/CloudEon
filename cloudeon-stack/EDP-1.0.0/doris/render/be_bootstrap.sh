#!/bin/bash
basedir=$(cd `dirname $0`/..; pwd)
sysctl -w vm.max_map_count=2000000
$DORIS_HOME/doris-be/bin/start_be.sh --daemon
$DORIS_HOME/doris-be/lib/apache_hdfs_broker/bin/start_broker.sh --daemon

tail -f /dev/null

