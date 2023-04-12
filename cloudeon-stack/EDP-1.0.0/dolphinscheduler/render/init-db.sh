#!/bin/bash


DOLPHINSCHEDULER_TOOLS_HOME=$DOLPHINSCHEDULER_HOME/tools

pid=/opt/edp/${service.serviceName}/data/pid
log=/opt/edp/${service.serviceName}/logs/tools-$HOSTNAME.out
DS_CONF=/opt/edp/${service.serviceName}/conf

source "$DS_CONF/dolphinscheduler_env.sh"

JAVA_OPTS=${JAVA_OPTS:-"-server -Duser.timezone=${SPRING_JACKSON_TIME_ZONE} -Xms1g -Xmx1g -Xmn512m -XX:+PrintGCDetails -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof"}


JAVA_OPTS="${JAVA_OPTS} -XX:-UseContainerSupport"

nohup java $JAVA_OPTS \
        -cp "$DS_CONF/common.properties":"$DOLPHINSCHEDULER_TOOLS_HOME/libs/*":"$DOLPHINSCHEDULER_TOOLS_HOME/sql" \
        -Dspring.profiles.active=upgrade,mysql   \
        org.apache.dolphinscheduler.tools.datasource.UpgradeDolphinScheduler   --spring.config.location=$DS_CONF/tool-application.yaml  > $log 2>&1 &

echo $! > $pid
