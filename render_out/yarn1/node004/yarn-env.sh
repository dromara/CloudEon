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
export HADOOP_YARN_USER=${HADOOP_YARN_USER:-yarn}

# resolve links - $0 may be a softlink
export YARN_LOG_DIR=/var/log/YARN1
export HADOOP_CONF_DIR=/etc/YARN1/conf
export HDFS_CONF_DIR=/etc/HDFS1/conf
export YARN_RESOURCEMANAGER_ADDRRESS=node001
export YARN_TIMELINE_SERVICE_HOSTNAME=node003


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

export YARN_NODEMANAGER_HEAPSIZE=1,024m

export YARN_NODEMANAGER_HEAPSIZE=1,024m

export YARN_HISTORYSERVER_HEAPSIZE=1,024m

export YARN_TIMELINESERVER_HEAPSIZE=1,024m

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


cp /etc/YARN1/conf/krb5.conf /etc
export MASTERPRINCIPAL=yarn/node004
export KEYTAB=/etc/YARN1/conf/yarn.keytab
export KRB_PLUGIN_ENABLE=true
export YARN_RESOURCEMANAGER_OPTS="-Djava.security.auth.login.config=/etc/YARN1/conf/jaas.conf"
