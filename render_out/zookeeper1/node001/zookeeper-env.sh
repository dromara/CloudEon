#!/usr/bin/env bash

export ZOOKEEPER_LOG_DIR=/var/log/ZOOKEEPER1
export ZOOKEEPER_DATA_DIR=/var/ZOOKEEPER1

export SERVER_JVMFLAGS="-Dcom.sun.management.jmxremote.port=9898 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false"

export SERVER_JVMFLAGS="-Xmx9,096m $SERVER_JVMFLAGS"

export SERVER_JVMFLAGS="-Dzookeeper.log.dir=/var/log/ZOOKEEPER1 -Dzookeeper.root.logger=INFO,ROLLINGFILE $SERVER_JVMFLAGS"

export SERVER_JVMFLAGS="-Dznode.container.checkIntervalMs=3000 $SERVER_JVMFLAGS"

export SERVER_JVMFLAGS="$SERVER_JVMFLAGS -Djava.security.auth.login.config=/etc/ZOOKEEPER1/conf/jaas.conf  -Dzookeeper.allowSaslFailedClients=false"
export KRB_KEYTAB=zookeeper.keytab
chown zookeeper:zookeeper $KRB_KEYTAB
chown zookeeper:zookeeper /etc/ZOOKEEPER1/conf/jaas.conf
chmod 400 $KRB_KEYTAB
cp /etc/ZOOKEEPER1/conf/krb5.conf /etc
export JAVAAGENT_OPTS=" -javaagent:/usr/lib/jmx_exporter/jmx_prometheus_javaagent-0.7.jar=5556:/usr/lib/jmx_exporter/agentconfig.yml "
