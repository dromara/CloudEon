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
export YARN_LOG_DIR=/workspace/logs

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
<#assign yarnHeapRamPercentage = conf['yarn.heap.memory.percentage']?trim?number>
if [ -z $yarnHeapRam ];then
    if [ -z $MEM_LIMIT ];then
        export yarnHeapRam=2048M
    else
        export yarnHeapRam=$[ $MEM_LIMIT / 1024 / 1024  * ${yarnHeapRamPercentage} / 100 ]M
    fi

fi

export HADOOP_HEAPSIZE_MAX=$yarnHeapRam
export HADOOP_HEAPSIZE_MIN=$yarnHeapRam

# 添加jmx监控开放
export YARN_NODEMANAGER_OPTS="$YARN_NODEMANAGER_OPTS -Dcom.sun.management.jmxremote.port=9917 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent.jar=5547:$HADOOP_CONF_DIR/jmx_prometheus.yaml -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/workspace/logs/gc-hadoop-yarn-resourcemanager.log"
export YARN_RESOURCEMANAGER_OPTS="$YARN_RESOURCEMANAGER_OPTS -Dcom.sun.management.jmxremote.port=9918 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent.jar=5548:$HADOOP_CONF_DIR/jmx_prometheus.yaml   -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/workspace/logs/gc-hadoop-yarn-nodemanager.log"


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