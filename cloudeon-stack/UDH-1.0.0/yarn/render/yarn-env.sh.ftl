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

# User for YARN daemons
export HADOOP_YARN_USER=${r"${HADOOP_YARN_USER:-yarn}"}

# resolve links - $0 may be a softlink
export YARN_LOG_DIR=/opt/udh/${service.serviceName}/log
export HADOOP_CONF_DIR=/opt/udh/${service.serviceName}/conf
export HDFS_CONF_DIR=/opt/udh/${service.serviceName}/conf
export NODEMANAGER_LOCAL_DIRS=/opt/udh/${service.serviceName}/data/local

export YARN_RESOURCEMANAGER_ADDRRESS=${serviceRoles['YARN_RESOURCEMANAGER'][0]['hostname']}
<#if serviceRoles['YARN_TIMELINESERVER']??>
export YARN_TIMELINE_SERVICE_HOSTNAME=${serviceRoles['YARN_TIMELINESERVER'][0]['hostname']}
</#if>


# some Java parameters
# export JAVA_HOME=/home/y/libexec/jdk1.6.0/
if [ "$JAVA_HOME" != "" ]; then
  #echo "run java in $JAVA_HOME"
  JAVA_HOME=$JAVA_HOME
fi

if [ "$JAVA_HOME" = "" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi

JAVA=$JAVA_HOME/bin/java
JAVA_HEAP_MAX=-Xmx4096m
<#--resource manager memory-->
<#if conf['resourcemanager.container.limits.memory'] != "-1" && conf['resourcemanager.memory.ratio'] != "-1">
  <#assign limitsMemory = conf['resourcemanager.container.limits.memory']?number
  memoryRatio = conf['resourcemanager.memory.ratio']?number
  resourcemanagerMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if conf['resourcemanager.memory']??>
    <#assign resourcemanagerMemory=conf['resourcemanager.memory']?trim?number>
  <#else>
    <#assign resourcemanagerMemory=4096>
  </#if>
</#if>
export YARN_RESOURCEMANAGER_HEAPSIZE=${resourcemanagerMemory?floor?c}m
<#--node manager memory-->
<#if conf['nodemanager.container.limits.memory'] != "-1" && conf['nodemanager.memory.ratio'] != "-1">
  <#assign limitsMemory = conf['nodemanager.container.limits.memory']?number
  memoryRatio = conf['nodemanager.memory.ratio']?number
  nodemanagerMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if conf['nodemanager.memory']??>
    <#assign nodemanagerMemory=conf['nodemanager.memory']?trim?number>
  <#else>
    <#assign nodemanagerMemory=4096>
  </#if>
</#if>
export YARN_NODEMANAGER_HEAPSIZE=${nodemanagerMemory?floor?c}m
<#--historyserver memory-->
<#if conf['historyserver.container.limits.memory'] != "-1" && conf['historyserver.memory.ratio'] != "-1">
  <#assign limitsMemory = conf['historyserver.container.limits.memory']?number
  memoryRatio = conf['historyserver.memory.ratio']?number
  historyserverMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if conf['historyserver.memory']??>
    <#assign historyserverMemory=conf['historyserver.memory']?trim?number>
  <#else>
    <#assign historyserverMemory=4096>
  </#if>
</#if>
export YARN_HISTORYSERVER_HEAPSIZE=${historyserverMemory?floor?c}m
<#--timelineserver memory-->
<#if conf['timelineserver.container.limits.memory'] != "-1" && conf['timelineserver.memory.ratio'] != "-1">
  <#assign limitsMemory = conf['timelineserver.container.limits.memory']?number
  memoryRatio = conf['timelineserver.memory.ratio']?number
  timelineserverMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if conf['timelineserver.memory']??>
    <#assign timelineserverMemory=conf['timelineserver.memory']?trim?number>
  <#else>
    <#assign timelineserverMemory=4096>
  </#if>
</#if>
export YARN_TIMELINESERVER_HEAPSIZE=${timelineserverMemory?floor?c}m

# 添加jmx监控开放
export YARN_NODEMANAGER_OPTS="$YARN_NODEMANAGER_OPTS -Dcom.sun.management.jmxremote.port=9917 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent-0.14.0.jar=5547:/opt/udh/${service.serviceName}/conf/jmx_prometheus.yaml"
export YARN_RESOURCEMANAGER_OPTS="$YARN_RESOURCEMANAGER_OPTS -Dcom.sun.management.jmxremote.port=9918 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent-0.14.0.jar=5548:/opt/udh/${service.serviceName}/conf/jmx_prometheus.yaml"

# check envvars which might override default args
if [ "$YARN_HEAPSIZE" != "" ]; then
  #echo "run with heapsize $YARN_HEAPSIZE"
  JAVA_HEAP_MAX="-Xmx""$YARN_HEAPSIZE""m"
  #echo $JAVA_HEAP_MAX
fi

# so that filenames w/ spaces are handled correctly in loops below
IFS=


# default log directory & file
if [ "$YARN_LOG_DIR" = "" ]; then
  YARN_LOG_DIR="$YARN_HOME/logs"
fi
if [ "$YARN_LOGFILE" = "" ]; then
  YARN_LOGFILE='yarn.log'
fi

# default policy file for service-level authorization
if [ "$YARN_POLICYFILE" = "" ]; then
  YARN_POLICYFILE="hadoop-policy.xml"
fi

# restore ordinary behaviour
unset IFS

