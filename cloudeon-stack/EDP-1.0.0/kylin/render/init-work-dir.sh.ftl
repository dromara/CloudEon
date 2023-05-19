#!/bin/bash


HADOOP_CONF_DIR="/home/hadoop/apache-kylin/conf"
HDFS_HOME="${r"${HADOOP_HOME}"}"
KYLIN_WORK_DIR="${conf['kylin.hdfs.work.dir']}"



/bin/bash -c "${r"${HDFS_HOME}"}/bin/hadoop --config ${r"${HADOOP_CONF_DIR}"}   fs -test -e  ${r"${KYLIN_WORK_DIR}"}"
if [ $? -eq 0 ] ;then
    echo "${r"${KYLIN_WORK_DIR}"} already exists."
else
    echo "${r"${KYLIN_WORK_DIR}"} does not exist."
    /bin/bash -c "${r"${HDFS_HOME}"}/bin/hadoop --config ${r"${HADOOP_CONF_DIR}"} fs  -mkdir -p ${r"${KYLIN_WORK_DIR}"}"
    /bin/bash -c "${r"${HDFS_HOME}"}/bin/hadoop --config ${r"${HADOOP_CONF_DIR}"} fs  -chmod  -R 777 ${r"${KYLIN_WORK_DIR}"}"
    echo " create ${r"${KYLIN_WORK_DIR}"} on hdfs successfully."
fi

