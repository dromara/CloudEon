#!/bin/bash



HDFS_HOME="$HADOOP_HOME"
FLINK_HISTORY_LOGS_DIR="${conf['flink.history.fs.logDirectory']}"




 /bin/bash -c "$HDFS_HOME/bin/hadoop --config  $HDFS_CONF_DIR  fs -test -e  $FLINK_HISTORY_LOGS_DIR"
if [ $? -eq 0 ] ;then
    echo "$FLINK_HISTORY_LOGS_DIR already exists."
else
    echo "$FLINK_HISTORY_LOGS_DIR does not exist."
     /bin/bash -c "$HDFS_HOME/bin/hadoop --config  $HDFS_CONF_DIR  fs -mkdir -p $FLINK_HISTORY_LOGS_DIR"
     /bin/bash -c "$HDFS_HOME/bin/hadoop --config  $HDFS_CONF_DIR  fs -chmod  -R 777 $FLINK_HISTORY_LOGS_DIR"
fi

