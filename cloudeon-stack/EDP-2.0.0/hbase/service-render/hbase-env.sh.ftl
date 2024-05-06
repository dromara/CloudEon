#!/usr/bin/env bash
#
#/**
# * Licensed to the Apache Software Foundation (ASF) under one
# * or more contributor license agreements.  See the NOTICE file
# * distributed with this work for additional information
# * regarding copyright ownership.  The ASF licenses this file
# * to you under the Apache License, Version 2.0 (the
# * "License"); you may not use this file except in compliance
# * with the License.  You may obtain a copy of the License at
# *
# *     http://www.apache.org/licenses/LICENSE-2.0
# *
# * Unless required by applicable law or agreed to in writing, software
# * distributed under the License is distributed on an "AS IS" BASIS,
# * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# * See the License for the specific language governing permissions and
# * limitations under the License.
# */

# Set environment variables here.

<#assign ramPercentage = conf['hbase.heap.memory.percentage']?number>
export HBASE_HEAP_MEMORY=$[ $MEM_LIMIT / 1024 / 1024  * ${ramPercentage} / 100 ]M
export HBASE_MASTER_OPTS=" -Xmx$HBASE_HEAP_MEMORY -Xms$HBASE_HEAP_MEMORY  $HBASE_MASTER_OPTS"
export HBASE_REGIONSERVER_OPTS=" -Xmx$HBASE_HEAP_MEMORY -Xms$HBASE_HEAP_MEMORY $HBASE_REGIONSERVER_OPTS"

# Uncomment and adjust to enable JMX exporting
# See jmxremote.password and jmxremote.access in $JRE_HOME/lib/management to configure remote password access.
# More details at: http://java.sun.com/javase/6/docs/technotes/guides/management/agent.html
# NOTE: HBase provides an alternative JMX implementation to fix the random ports issue, please see JMX
# section in HBase Reference Guide for instructions.

export HBASE_JMX_BASE="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
export HBASE_MASTER_OPTS="$HBASE_MASTER_OPTS $HBASE_JMX_BASE -Dcom.sun.management.jmxremote.port=9922 -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent.jar=5552:$HBASE_HOME/conf/jmx_prometheus.yaml -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/workspace/logs/gc-hbase-master.log"
export HBASE_REGIONSERVER_OPTS="$HBASE_REGIONSERVER_OPTS $HBASE_JMX_BASE -Dcom.sun.management.jmxremote.port=9923 -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent.jar=5553:$HBASE_HOME/conf/jmx_prometheus.yaml -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/workspace/logs/gc-hbase-regionserver.log"
# export HBASE_THRIFT_OPTS="$HBASE_THRIFT_OPTS $HBASE_JMX_BASE -Dcom.sun.management.jmxremote.port=10103"
# export HBASE_ZOOKEEPER_OPTS="$HBASE_ZOOKEEPER_OPTS $HBASE_JMX_BASE -Dcom.sun.management.jmxremote.port=10104"
# export HBASE_REST_OPTS="$HBASE_REST_OPTS $HBASE_JMX_BASE -Dcom.sun.management.jmxremote.port=10105"


# Where log files are stored.  $HBASE_HOME/logs by default.
export HBASE_LOG_DIR=/workspace/logs



# The directory where pid files are stored. /tmp by default.
export HBASE_PID_DIR=/workspace/data


# Tell HBase whether it should manage it's own instance of ZooKeeper or not.
export HBASE_MANAGES_ZK=false


