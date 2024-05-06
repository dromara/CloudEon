#!/bin/bash

SPARK_HISTORY_LOGS_DIR="${conf['spark.history.fs.logDirectory']}"

set +e
hadoop fs -test -e  $SPARK_HISTORY_LOGS_DIR
if [ $? -eq 0 ] ;then
    echo "$SPARK_HISTORY_LOGS_DIR already exists."
else
    echo "$SPARK_HISTORY_LOGS_DIR does not exist."
    hadoop fs -mkdir -p $SPARK_HISTORY_LOGS_DIR
    hadoop fs -chmod -R 777 $SPARK_HISTORY_LOGS_DIR
fi

