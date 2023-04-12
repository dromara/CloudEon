#!/bin/bash


DOLPHINSCHEDULER_MASTER_HOME=$DOLPHINSCHEDULER_HOME/master-server

pid=/opt/edp/${service.serviceName}/data/pid
log=/opt/edp/${service.serviceName}/logs/master-server-$HOSTNAME.out
DS_CONF=/opt/edp/${service.serviceName}/conf

source "$DS_CONF/dolphinscheduler_env.sh"

JAVA_OPTS="-server -Duser.timezone=$SPRING_JACKSON_TIME_ZONE -Xms1g -Xmx1g -Xmn512m -XX:+PrintGCDetails -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof"


JAVA_OPTS="$JAVA_OPTS -XX:-UseContainerSupport"

nohup java $JAVA_OPTS \
  -cp "$DS_CONF/common.properties":"$DOLPHINSCHEDULER_MASTER_HOME/libs/*" \
-Dlogging.config=$DS_CONF/master-logback.xml   org.apache.dolphinscheduler.server.master.MasterServer   --spring.config.location=$DS_CONF/master-application.yaml  > $log 2>&1 &

echo $! > $pid
