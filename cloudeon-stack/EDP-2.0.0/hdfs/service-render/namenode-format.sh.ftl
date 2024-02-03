#!/usr/bin/env bash

set -o errexit
set -o errtrace
set -o pipefail
set -o xtrace

_METADATA_DIR=/data/1/namenode/current

<#assign namenodes=serviceRoles["HDFS_NAMENODE"]>

<#if namenodes?size == 2>
    <#if namenodes[0].id lt namenodes[1].id>
        <#assign nn1=namenodes[0]>
        <#assign nn2=namenodes[1]>
    <#else>
        <#assign nn1=namenodes[1]>
        <#assign nn2=namenodes[0]>
    </#if>

    <#if nn1.hostname == HOSTNAME || nn1.hostname == NODE_NAME>
 if [[ ! -d ${r"$_METADATA_DIR"} ]]; then
     echo "无法检测到namenode元数据文件夹，开始进行namenode格式化。。。。。。。。。。"
     hdfs namenode -format ${conf['nameservices']}  -force -nonInteractive
 fi
    <#elseif nn2.hostname == HOSTNAME || nn2.hostname == NODE_NAME>
 if [[ ! -d ${r"$_METADATA_DIR"} ]]; then
   echo "检测到没有namenode的元数据文件夹，开始进行namenode的初始化操作，从checkpoint中加载。。。。。。。"
   # 第一次运行可能会报错重启，这是因为主节点还没有初始化完毕：ERROR ha.BootstrapStandby: Unable to fetch namespace information from any remote NN
   hdfs namenode -bootstrapStandby -force -nonInteractive
 fi
    </#if>

</#if>
