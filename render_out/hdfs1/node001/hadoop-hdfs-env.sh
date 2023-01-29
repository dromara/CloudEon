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
# export JAVA_HOME=${JAVA_HOME}/bin
export JAVA_HOME=/usr/java/latest

# The jsvc implementation to use. Jsvc is required to run secure datanodes.

export HADOOP_CONF_DIR=/etc/HDFS1/conf


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
export NAMENODE_MEMORY=24,000m
export HADOOP_NAMENODE_OPTS="-Xmx24,000m -XX:+UseConcMarkSweepGC -XX:+ExplicitGCInvokesConcurrent -Dcom.sun.management.jmxremote $HADOOP_NAMENODE_OPTS"
export HADOOP_SECONDARYNAMENODE_OPTS="-Xmx24,000m -Dcom.sun.management.jmxremote $HADOOP_SECONDARYNAMENODE_OPTS"

# Export zkfc memory
export ZKFC_MEMORY=1,024m
export HADOOP_ZKFC_OPTS="-Xmx1,024m $HADOOP_ZKFC_OPTS"

# Export datanode memory
export DATANODE_MEMORY=8,192m
export HADOOP_DATANODE_OPTS="-Xmx8,192m -Dcom.sun.management.jmxremote $HADOOP_DATANODE_OPTS"

# Export journalnode memory
export JOURNALNODE_MEMORY=4,096m
export HADOOP_JOURNALNODE_OPTS="-Xmx4,096m $HADOOP_JOURNALNODE_OPTS"

export HADOOP_BALANCER_OPTS="-Xmx4096m -Dcom.sun.management.jmxremote $HADOOP_BALANCER_OPTS"

# The following applies to multiple commands (fs, dfs, fsck, distcp etc)
#export HADOOP_CLIENT_OPTS="-Xmx128m $HADOOP_CLIENT_OPTS"
#HADOOP_JAVA_PLATFORM_OPTS="-XX:-UsePerfData $HADOOP_JAVA_PLATFORM_OPTS"

# On secure datanodes, user to run the datanode as after dropping privileges
export HADOOP_SECURE_DN_USER=${HADOOP_SECURE_DN_USER}

# Where log files are stored.  $HADOOP_HOME/logs by default.
export HADOOP_LOG_DIR=/var/log/HDFS1

# Where log files are stored in the secure data environment.
export HADOOP_SECURE_DN_LOG_DIR=${HADOOP_LOG_DIR}

# The directory where pid files are stored. /tmp by default.
export HADOOP_PID_DIR=/var/run/HDFS1
export HADOOP_SECURE_DN_PID_DIR=${HADOOP_PID_DIR}

# A string representing this instance of hadoop. $USER by default.
#export HADOOP_IDENT_STRING=$USER

export CLUSTER=HDFS1


export NAMENODE_NAMESERVICE=nameservice1
export HDFS_HA=true
export NAMENODE_ORDINAL=0


# Export journalnode config
export JOURNALNODE_HTTP_PORT=7809
export JOURNALNODE_RPC_PORT=9809
# export HDFS_JOURNAL_NODE_COUNT=3

# Export dfs.datanode.data.dir

# Export dfs.namenode.name.dir


cp /etc/HDFS1/conf/krb5.conf /etc/
export MASTERPRINCIPAL=hdfs/node001
export KEYTAB=/etc/HDFS1/conf/hdfs.keytab
export KRB_PLUGIN_ENABLE=true
export ZKFC_OPTS="-Djava.security.auth.login.config=/etc/HDFS1/conf/jaas.conf"
