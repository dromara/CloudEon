export ZOOPIDFILE="/var/run/${service.sid}/zookeeper-server.pid"
export ZOOKEEPER_CONF="/etc/${service.sid}/conf"
export ZOO_LOG_DIR="/var/log/${service.sid}"
export SERVER_JVMFLAGS="-Dcom.sun.management.jmxremote.port=${service['zookeeper.jmxremote.port']} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false $SERVER_JVMFLAGS"
