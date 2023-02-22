cluster_id=${conf['cluster_id']}

# the output dir of stderr and stdout
LOG_DIR = /opt/udh/${service.serviceName}/log

DATE = `date +%Y%m%d-%H%M%S`
JAVA_OPTS="-Xmx8192m -XX:+UseMembar -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=7 -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSClassUnloadingEnabled -XX:-CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=80 -XX:SoftRefLRUPolicyMSPerMB=0 -Xloggc:/opt/udh/${service.serviceName}/log/fe.gc.log.$DATE"

# For jdk 9+, this JAVA_OPTS will be used as default JVM options
JAVA_OPTS_FOR_JDK_9="-Xmx8192m -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=7 -XX:+CMSClassUnloadingEnabled -XX:-CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=80 -XX:SoftRefLRUPolicyMSPerMB=0 -Xlog:gc*:/opt/udh/${service.serviceName}/log/fe.gc.log.$DATE:time"


# INFO, WARN, ERROR, FATAL
sys_log_level = INFO

# store metadata, must be created before start FE.
meta_dir =  /opt/udh/${service.serviceName}/data

http_port = ${conf['fe_http_port']}
rpc_port = ${conf['rpc_port']}
query_port = ${conf['query_port']}
edit_log_port = ${conf['edit_log_port']}
mysql_service_nio_enabled = true

audit_log_dir = /opt/udh/${service.serviceName}/log
sys_log_dir = /opt/udh/${service.serviceName}/log

priority_networks = ${conf['fe_priority_networks']}


