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
export YARN_LOG_DIR=/var/log/${service.sid}
export HADOOP_CONF_DIR=/etc/${service.sid}/conf
<#-- HDFS_CONF_DIR should be /etc/${dependencies.HDFS.sid}/conf,
     otherwise TDH-Client downloader cannot find which HDFS this YARN depends on -->
export HDFS_CONF_DIR=/etc/${dependencies.HDFS.sid}/conf
export YARN_RESOURCEMANAGER_ADDRRESS=${service.roles.YARN_RESOURCEMANAGER[0]['hostname']}
<#if service['yarn.nodemanager.local-dirs']??>
export NODEMANAGER_LOCAL_DIRS=${service['yarn.nodemanager.local-dirs']}
</#if>
<#if service['yarn.nodemanager.local-dirs']??>
export NODEMANAGER_LOG_DIRS=${service['yarn.nodemanager.log-dirs']}
</#if>
<#if service.roles.YARN_TIMELINESERVER??>
export YARN_TIMELINE_SERVICE_HOSTNAME=${service.roles.YARN_TIMELINESERVER[0]['hostname']}
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

<#if service['resourcemanager.container.limits.memory'] != "-1" && service['resourcemanager.memory.ratio'] != "-1">
  <#assign limitsMemory = service['resourcemanager.container.limits.memory']?number
  memoryRatio = service['resourcemanager.memory.ratio']?number
  resourcemanagerMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if service[.data_model["localhostname"]]['resourcemanager.memory']??>
    <#assign resourcemanagerMemory=service[.data_model["localhostname"]]['resourcemanager.memory']?trim?number>
  <#else>
    <#assign resourcemanagerMemory=4096>
  </#if>
</#if>
export YARN_NODEMANAGER_HEAPSIZE=${resourcemanagerMemory?floor}m

<#if service['nodemanager.container.limits.memory'] != "-1" && service['nodemanager.memory.ratio'] != "-1">
  <#assign limitsMemory = service['nodemanager.container.limits.memory']?number
  memoryRatio = service['nodemanager.memory.ratio']?number
  nodemanagerMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if service[.data_model["localhostname"]]['nodemanager.memory']??>
    <#assign nodemanagerMemory=service[.data_model["localhostname"]]['nodemanager.memory']?trim?number>
  <#else>
    <#assign nodemanagerMemory=4096>
  </#if>
</#if>
export YARN_NODEMANAGER_HEAPSIZE=${nodemanagerMemory?floor}m

<#if service['historyserver.container.limits.memory'] != "-1" && service['historyserver.memory.ratio'] != "-1">
  <#assign limitsMemory = service['historyserver.container.limits.memory']?number
  memoryRatio = service['historyserver.memory.ratio']?number
  historyserverMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if service[.data_model["localhostname"]]['historyserver.memory']??>
    <#assign historyserverMemory=service[.data_model["localhostname"]]['historyserver.memory']?trim?number>
  <#else>
    <#assign historyserverMemory=4096>
  </#if>
</#if>
export YARN_HISTORYSERVER_HEAPSIZE=${historyserverMemory?floor}m

<#if service['timelineserver.container.limits.memory'] != "-1" && service['timelineserver.memory.ratio'] != "-1">
  <#assign limitsMemory = service['timelineserver.container.limits.memory']?number
  memoryRatio = service['timelineserver.memory.ratio']?number
  timelineserverMemory = limitsMemory * memoryRatio * 1024>
<#else>
  <#if service[.data_model["localhostname"]]['timelineserver.memory']??>
    <#assign timelineserverMemory=service[.data_model["localhostname"]]['timelineserver.memory']?trim?number>
  <#else>
    <#assign timelineserverMemory=4096>
  </#if>
</#if>
export YARN_TIMELINESERVER_HEAPSIZE=${timelineserverMemory?floor}m

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

<#if service.plugins?seq_contains("guardian")>
cp /etc/${service.sid}/conf/krb5.conf /etc
export MASTERPRINCIPAL=yarn/${localhostname}
export KEYTAB=/etc/${service.sid}/conf/yarn.keytab
export KRB_PLUGIN_ENABLE=true
</#if>

<#if service.auth = "kerberos">
cp /etc/${service.sid}/conf/krb5.conf /etc
export MASTERPRINCIPAL=yarn/${localhostname}
export KEYTAB=/etc/${service.sid}/conf/yarn.keytab
export KRB_PLUGIN_ENABLE=true
export YARN_RESOURCEMANAGER_OPTS="-Djava.security.auth.login.config=/etc/${service.sid}/conf/jaas.conf"
</#if>
