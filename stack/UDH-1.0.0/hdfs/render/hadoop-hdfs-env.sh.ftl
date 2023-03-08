#!/usr/bin/env bash
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Set Hadoop-specific environment variables here.

# The only required environment variable is JAVA_HOME.  All others are
# optional.  When running a distributed configuration it is best to
# set JAVA_HOME in this file, so that it is correctly defined on
# remote nodes.

# The java implementation to use.
export JAVA_HOME=${r"${JAVA_HOME}"}

# The jsvc implementation to use. Jsvc is required to run secure datanodes.

export HADOOP_CONF_DIR=/opt/udh/${service.serviceName}/conf


# Extra Java CLASSPATH elements.  Automatically insert capacity-scheduler.
for f in $HADOOP_HOME/contrib/capacity-scheduler/*.jar; do
if [ "$HADOOP_CLASSPATH" ]; then
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:$f
else
export HADOOP_CLASSPATH=$f
fi
done


# The maximum amount of heap to use, in MB. Default is 1000.
#export HADOOP_HEAPSIZE=
#export HADOOP_NAMENODE_INIT_HEAPSIZE=""

# Extra Java runtime options.  Empty by default.
export HADOOP_OPTS="$HADOOP_OPTS -Djava.net.preferIPv4Stack=true $HADOOP_CLIENT_OPTS"

# Command specific options appended to HADOOP_OPTS when specified
# Export namenode memory
<#if conf['namenode.container.limits.memory'] != "-1" && conf['namenode.memory.ratio'] != "-1">
  <#assign limitsMemory = conf['namenode.container.limits.memory']?number
    memoryRatio = conf['namenode.memory.ratio']?number
    namenodeMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if conf['namenode.memory']??>
    <#assign namenodeMemory=conf['namenode.memory']?trim?number>
  <#else>
    <#assign namenodeMemory=24000>
  </#if>
</#if>
export NAMENODE_MEMORY=${namenodeMemory?floor?c}m
export HADOOP_NAMENODE_OPTS="-Xmx${namenodeMemory?floor?c}m -XX:+UseConcMarkSweepGC -XX:+ExplicitGCInvokesConcurrent -Dcom.sun.management.jmxremote $HADOOP_NAMENODE_OPTS"
export HADOOP_SECONDARYNAMENODE_OPTS="-Xmx${namenodeMemory?floor?c}m -Dcom.sun.management.jmxremote $HADOOP_SECONDARYNAMENODE_OPTS"

# Export zkfc memory
<#if conf['zkfc.container.limits.memory'] != "-1" && conf['zkfc.memory.ratio'] != "-1">
  <#assign limitsMemory = conf['zkfc.container.limits.memory']?number
    memoryRatio = conf['zkfc.memory.ratio']?number
    zkfcMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if  conf['zkfc.memory']??>
    <#assign zkfcMemory=conf['zkfc.memory']?trim?number>
  <#else>
    <#assign zkfcMemory=1024>
  </#if>
</#if>
export ZKFC_MEMORY=${zkfcMemory?floor?c}m
export HADOOP_ZKFC_OPTS="-Xmx${zkfcMemory?floor?c}m $HADOOP_ZKFC_OPTS"

# Export datanode memory
<#if conf['datanode.container.limits.memory'] != "-1" && conf['datanode.memory.ratio'] != "-1">
    <#assign limitsMemory = conf['datanode.container.limits.memory']?number
    memoryRatio = conf['datanode.memory.ratio']?number
    datanodeMemory = limitsMemory * memoryRatio * 1024>
<#else>
    <#if  conf['datanode.memory']??>
        <#assign datanodeMemory=conf['datanode.memory']?trim?number>
    <#else>
        <#assign datanodeMemory=8192>
    </#if>
</#if>
export DATANODE_MEMORY=${datanodeMemory?floor?c}m
export HADOOP_DATANODE_OPTS="-Xmx${datanodeMemory?floor?c}m -Dcom.sun.management.jmxremote $HADOOP_DATANODE_OPTS"

# Export journalnode memory
<#if conf['journalnode.container.limits.memory'] != "-1" && conf['journalnode.memory.ratio'] != "-1">
  <#assign limitsMemory = conf['journalnode.container.limits.memory']?number
    memoryRatio = conf['journalnode.memory.ratio']?number
    journalnodeMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if conf['journalnode.memory']??>
    <#assign journalnodeMemory=conf['journalnode.memory']?trim?number>
  <#else>
    <#assign journalnodeMemory=4096>
  </#if>
</#if>
export JOURNALNODE_MEMORY=${journalnodeMemory?floor?c}m
export HADOOP_JOURNALNODE_OPTS="-Xmx${journalnodeMemory?floor?c}m $HADOOP_JOURNALNODE_OPTS"

export HADOOP_BALANCER_OPTS="-Xmx4096m -Dcom.sun.management.jmxremote $HADOOP_BALANCER_OPTS"

# The following applies to multiple commands (fs, dfs, fsck, distcp etc)
#export HADOOP_CLIENT_OPTS="-Xmx128m $HADOOP_CLIENT_OPTS"
#HADOOP_JAVA_PLATFORM_OPTS="-XX:-UsePerfData $HADOOP_JAVA_PLATFORM_OPTS"

# On secure datanodes, user to run the datanode as after dropping privileges
export HADOOP_SECURE_DN_USER=${r"${HADOOP_SECURE_DN_USER}"}

# Where log files are stored.  $HADOOP_HOME/logs by default.
export HADOOP_LOG_DIR=/opt/udh/${service.serviceName}/log

# Where log files are stored in the secure data environment.
export HADOOP_SECURE_DN_LOG_DIR=${r"${HADOOP_LOG_DIR}"}

# The directory where pid files are stored. /tmp by default.
export HADOOP_PID_DIR=/opt/udh/${service.serviceName}/data
export HADOOP_SECURE_DN_PID_DIR=${r"${HADOOP_PID_DIR}"}

# A string representing this instance of hadoop. $USER by default.
#export HADOOP_IDENT_STRING=$USER

<#--export CLUSTER=${service.serviceName}-->

<#--<#assign namenodes=serviceRoles["HDFS_NAMENODE"]>-->
<#--<#if namenodes?size == 1>-->
<#--    <#if namenodes[0].hostname == .data_model["localhostname"]>-->
<#--        export NAMENODE_NAMESERVICE=${service.serviceName}-->
<#--        export HDFS_HA=false-->
<#--    </#if>-->
<#--<#elseif namenodes?size == 2>-->
<#--    <#if namenodes[0].id lt namenodes[1].id>-->
<#--        <#assign nn1=namenodes[0]>-->
<#--        <#assign nn2=namenodes[1]>-->
<#--    <#else>-->
<#--        <#assign nn1=namenodes[1]>-->
<#--        <#assign nn2=namenodes[0]>-->
<#--    </#if>-->

<#--    <#if nn1.hostname == .data_model["localhostname"]>-->
<#--        export NAMENODE_NAMESERVICE=${service.serviceName}-->
<#--        export HDFS_HA=true-->
<#--        export NAMENODE_ORDINAL=0-->
<#--    <#elseif nn2.hostname == .data_model["localhostname"]>-->
<#--        export NAMENODE_NAMESERVICE=${service.serviceName}-->
<#--        export HDFS_HA=true-->
<#--        export NAMENODE_ORDINAL=1-->
<#--        <#if conf["namenode.http-port"]??>-->
<#--            <#assign nn1HttpPort=conf["namenode.http-port"]>-->
<#--        </#if>-->
<#--        export NAMENODE_PRIMARY_JMX_URL=http://${nn1.hostname}:${nn1HttpPort}/jmx-->
<#--    </#if>-->
<#--<#else>-->
<#--    <#stop "more than 2 NameNodes in the same NameService not supported">-->
<#--</#if>-->



# Export journalnode config
<#if service['journalnode.http-port']??>
export JOURNALNODE_HTTP_PORT=${conf['journalnode.http-port']}
export JOURNALNODE_RPC_PORT=${conf['journalnode.rpc-port']}
# export HDFS_JOURNAL_NODE_COUNT=${serviceRoles.HDFS_JOURNALNODE?size}
</#if>

# Export dfs.datanode.data.dir
export DATANODE_DATA_DIRS=/opt/udh/${service.serviceName}/data/datanode

# Export dfs.namenode.name.dir
export NAMENODE_DATA_DIRS=/opt/udh/${service.serviceName}/data/namenode

