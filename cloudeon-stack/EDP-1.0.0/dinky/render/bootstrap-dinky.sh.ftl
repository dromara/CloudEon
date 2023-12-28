#!/bin/bash

FLINK_VERSION=1.15.4
FLINK_BIG_VERSION=1.15

pid=/opt/edp/${service.serviceName}/data/dinky-pid
log=/opt/edp/${service.serviceName}/log/dinky-$HOSTNAME.out
DINKY_CONF=/opt/edp/${service.serviceName}/conf

cp $FLINK_HOME/lib/flink-* $DINKY_HOME/plugins/flink$FLINK_BIG_VERSION/

CLASS_PATH="$DINKY_HOME/lib/*:config:$DINKY_HOME/plugins/*:$DINKY_HOME/plugins/flink$FLINK_BIG_VERSION/dinky/*:/opt/flink-$FLINK_VERSION/lib/*"

JAVA_OPTS="-server -Ddruid.mysql.usePingMethod=false -Duser.timezone=$SPRING_JACKSON_TIME_ZONE -Xms1g -Xmx1g -Xmn512m -XX:+PrintGCDetails -Xloggc:/opt/edp/${service.serviceName}/gc-dinky-server.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof  -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=9927 -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent-0.14.0.jar=5558:/opt/edp/${service.serviceName}/conf/jmx_prometheus.yaml"

cd $DINKY_HOME

rm -rf plugins/flink1.11
rm -rf plugins/flink1.12
rm -rf plugins/flink1.13
rm -rf plugins/flink1.14
rm -rf plugins/flink1.16
rm -rf plugins/flink1.17

cp jar/dlink-app-$FLINK_BIG_VERSION-$DINKY_VERSION-jar-with-dependencies.jar  dlink-app.jar
hdfs dfs -mkdir -p  /dlink/jar/
hdfs dfs -put dlink-app.jar /dlink/jar/
rm -rf dlink-app.jar
hdfs dfs -mkdir -p  /flink/lib/
hdfs dfs -put $FLINK_HOME/lib/*.jar  /flink/lib/
nohup java $JAVA_OPTS \
 -cp $CLASS_PATH \
-Dlogging.config=$DINKY_CONF/log4j2.xml  -Dspring.config.location=$DINKY_CONF/dinky-application.yaml,$DINKY_CONF/application.properties com.dlink.Dlink     > $log 2>&1 &

echo $! > $pid
tail -f /opt/edp/${service.serviceName}/log/dinky-$HOSTNAME.out