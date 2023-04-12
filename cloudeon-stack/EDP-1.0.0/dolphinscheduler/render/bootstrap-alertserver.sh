#!/bin/bash


DOLPHINSCHEDULER_ALERT_HOME=$DOLPHINSCHEDULER_HOME/alert-server

pid=/opt/edp/${service.serviceName}/data/pid
log=/opt/edp/${service.serviceName}/logs/alert-server-$HOSTNAME.out
DS_CONF=/opt/edp/${service.serviceName}/conf
source "$DS_CONF/dolphinscheduler_env.sh"

JAVA_OPTS=${JAVA_OPTS:-"-server -Duser.timezone=${SPRING_JACKSON_TIME_ZONE} -Xms1g -Xmx1g -Xmn512m -XX:+PrintGCDetails -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof"}


JAVA_OPTS="${JAVA_OPTS} -XX:-UseContainerSupport"

nohup java $JAVA_OPTS \
  -cp "$DS_CONF/common.properties":"$DOLPHINSCHEDULER_ALERT_HOME/libs/*" \
 -Dlogging.config=$DS_CONF/alert-logback.xml  org.apache.dolphinscheduler.alert.AlertServer    --spring.config.location=$DS_CONF/alert-application.yaml  > $log 2>&1 &

echo $! > $pid
