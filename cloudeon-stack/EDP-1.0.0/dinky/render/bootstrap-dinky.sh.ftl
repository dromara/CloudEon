#!/bin/bash

FLINK_VERSION=1.16

pid=/opt/edp/${service.serviceName}/data/dinky-pid
log=/opt/edp/${service.serviceName}/log/dinky-$HOSTNAME.out
DINKY_CONF=/opt/edp/${service.serviceName}/conf

cp $FLINK_HOME/lib/flink-* $DINKY_HOME/plugins/flink$FLINK_VERSION/

CLASS_PATH="$DINKY_HOME/lib/*:config:$DINKY_HOME/plugins/*:$DINKY_HOME/plugins/flink$FLINK_VERSION/*"

JAVA_OPTS="-server -Ddruid.mysql.usePingMethod=false -Duser.timezone=$SPRING_JACKSON_TIME_ZONE -Xms1g -Xmx1g -Xmn512m -XX:+PrintGCDetails -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof  -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=9927 -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent-0.14.0.jar=5558:/opt/edp/${service.serviceName}/conf/jmx_prometheus.yaml"

cd $DINKY_HOME

nohup java $JAVA_OPTS \
  -cp $CLASS_PATH \
 -Dlogging.config=$DINKY_CONF/log4j2.xml  -Dspring.config.location=$DINKY_CONF/dinky-application.yaml,$DINKY_CONF/application.properties com.dlink.Dlink     > $log 2>&1 &

echo $! > $pid
tail -f /dev/null