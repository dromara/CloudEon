#!/bin/bash


DOLPHINSCHEDULER_API_HOME=$DOLPHINSCHEDULER_HOME/api-server

pid=/opt/edp/${service.serviceName}/data/api-server-pid
log=/opt/edp/${service.serviceName}/log/api-server-$HOSTNAME.out
DS_CONF=/opt/edp/${service.serviceName}/conf
source "$DS_CONF/dolphinscheduler_env.sh"

JAVA_OPTS="-server -Duser.timezone=$SPRING_JACKSON_TIME_ZONE -Xms1g -Xmx1g -Xmn512m -XX:+PrintGCDetails -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof"




nohup java $JAVA_OPTS \
  -cp "$DS_CONF/common.properties":"$DOLPHINSCHEDULER_API_HOME/libs/*" \
 -Dlogging.config=$DS_CONF/api-logback.xml  org.apache.dolphinscheduler.api.ApiApplicationServer   --spring.config.location=$DS_CONF/api-application.yaml  > $log 2>&1 &

echo $! > $pid
tail -f /dev/null