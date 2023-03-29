#!/bin/bash

# example: /srv/udp/1.0.0.0/hive/bin/check-warehouse-dir.sh /srv/udp/1.0.0.0/yarn /user/hive/warehouse/ /tmp/

HDFS_HOME="$1"
HIVE_WAREHOUSE_DIR="$2"
HIVE_TMP_DIR="$3"



su -s /bin/bash hadoop -c "${HDFS_HOME}/bin/hadoop fs -test -e  ${HIVE_WAREHOUSE_DIR}"
if [ $? -eq 0 ] ;then
    echo "${HIVE_WAREHOUSE_DIR} already exists."
else
    echo "${HIVE_WAREHOUSE_DIR} does not exist."
    su -s /bin/bash hadoop -c "${HDFS_HOME}/bin/hadoop fs -mkdir -p ${HIVE_WAREHOUSE_DIR}"
    su -s /bin/bash hadoop -c "${HDFS_HOME}/bin/hadoop fs -chmod  -R 777 ${HIVE_WAREHOUSE_DIR}"
fi

su -s /bin/bash hadoop -c "${HDFS_HOME}/bin/hadoop fs -test -e  ${HIVE_TMP_DIR}"
if [ $? -eq 0 ] ;then
    echo "${HIVE_TMP_DIR} already exists."
else
    echo "$HIVE_WAREHOUSE_DIR does not exist."
    su -s /bin/bash hadoop -c "${HDFS_HOME}/bin/hadoop fs -mkdir -p ${HIVE_TMP_DIR}"
    su -s /bin/bash hadoop -c "${HDFS_HOME}/bin/hadoop fs -chmod  -R 777 ${HIVE_TMP_DIR}"
fi