#!/bin/bash
basedir=$(cd `dirname $0`/..; pwd)

$DORIS_HOME/doris-be/bin/start_be.sh --daemon

tail -f /dev/null

