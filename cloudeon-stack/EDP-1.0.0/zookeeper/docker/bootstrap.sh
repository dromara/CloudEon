#!/bin/bash
basedir=$(cd `dirname $0`/..; pwd)

$ZOOKEEPER_HOME/bin/zkServer.sh start

tail -f /dev/null

