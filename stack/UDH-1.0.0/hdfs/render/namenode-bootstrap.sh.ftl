#!/usr/bin/env bash
set -o errexit
set -o errtrace
set -o nounset
set -o pipefail
set -o xtrace

_HDFS_BIN=${r"${HADOOP_HOME}"}/bin/hdfs
_METADATA_DIR=${r"${NAMENODE_DATA_DIRS}"}/current
HADOOP_CONF_DIR=/opt/udh/${service.serviceName}/conf

source ${r"$HADOOP_CONF_DIR"}/hadoop-hdfs-env.sh



<#assign namenodes=serviceRoles["HDFS_NAMENODE"]>

<#if namenodes?size == 2>
    <#if namenodes[0].id lt namenodes[1].id>
        <#assign nn1=namenodes[0]>
        <#assign nn2=namenodes[1]>
    <#else>
        <#assign nn1=namenodes[1]>
        <#assign nn2=namenodes[0]>
    </#if>

    <#if nn1.hostname == .data_model["localhostname"]>
 if [[ ! -d ${r"$_METADATA_DIR"} ]]; then
     echo "无法检测到namenode元数据文件夹，开始进行namenode格式化。。。。。。。。。。"
     yes Y|  ${r"$_HDFS_BIN"} --config ${r"$HADOOP_CONF_DIR"} namenode -format  ${r"$CLUSTER"}
 fi
    <#elseif nn2.hostname == .data_model["localhostname"]>
 if [[ ! -d ${r"$_METADATA_DIR"} ]]; then
   echo "检测到没有namenode的元数据文件夹，开始进行namenode的初始化操作，从checkpoint中加载。。。。。。。"
   yes Y|   ${r"$_HDFS_BIN"} --config ${r"$HADOOP_CONF_DIR"} namenode -bootstrapStandby
 fi
    </#if>

</#if>

echo "========================start zkfc========================"
${r"${HADOOP_HOME}"}/sbin/hadoop-daemon.sh --config ${r"$HADOOP_CONF_DIR"} start zkfc
echo "========================start namenode========================"
${r"$_HDFS_BIN"} --config ${r"$HADOOP_CONF_DIR"} namenode