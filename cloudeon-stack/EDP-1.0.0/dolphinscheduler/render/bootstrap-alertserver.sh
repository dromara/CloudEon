#!/bin/bash


DOLPHINSCHEDULER_ALERT_HOME=$DOLPHINSCHEDULER_HOME/alert-server

pid=/opt/edp/${service.serviceName}/data/pid
log=/opt/edp/${service.serviceName}/logs/alert-server-$HOSTNAME.out

source "/opt/edp/${service.serviceName}/conf/dolphinscheduler_env.sh"

JAVA_OPTS=${JAVA_OPTS:-"-server -Duser.timezone=${SPRING_JACKSON_TIME_ZONE} -Xms1g -Xmx1g -Xmn512m -XX:+PrintGCDetails -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof"}


JAVA_OPTS="${JAVA_OPTS} -XX:-UseContainerSupport"

nohup java $JAVA_OPTS \
  -cp "/opt/edp/${service.serviceName}/conf/common.properties":"$DOLPHINSCHEDULER_ALERT_HOME/libs/*" \
 -Dlogging.config=/path/to/logback.xml  org.apache.dolphinscheduler.alert.AlertServer    --spring.config.location=/path/to/application.yml  > $log 2>&1 &

echo $! > $pid
