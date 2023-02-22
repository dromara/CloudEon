#!/bin/bash
basedir=$(cd `dirname $0`/..; pwd)

$DORIS_HOME/doris-fe/bin/start_fe.sh --daemon

tail -f /dev/null

