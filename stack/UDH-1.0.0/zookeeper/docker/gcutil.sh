#!/bin/bash
export PATH=/sbin:/bin:/usr/sbin:/usr/bin:/usr/local/sbin:/usr/local/bin

basedir=$(cd `dirname $0`/..; pwd)

checkUser() {
  if [ "`whoami`" == "root" ]; then
   echo "The root user is not allowed to run."
   exit 1
  fi
}
checkUser

jstat=${JAVA_HOME}/bin/jstat

app_name=zookeeper
app_pid=$(ps aux |grep java |grep  org.apache.zookeeper.server.quorum.QuorumPeerMain |awk '{print $2}')

echo "app_pid == $app_pid"
$jstat -gcutil $app_pid 1000