PPROF_TMPDIR="/opt/udh/${service.serviceName}/log"

# since 1.2, the JAVA_HOME need to be set to run BE process.
# JAVA_HOME=/path/to/jdk/

# INFO, WARNING, ERROR, FATAL
sys_log_level = ${conf['sys_log_level']}

# ports for admin, web, heartbeat service
be_port = ${conf['be_port']}
webserver_port = ${conf['webserver_port']}
heartbeat_service_port = ${conf['heartbeat_service_port']}
brpc_port =  ${conf['brpc_port']}

priority_networks = ${conf['be_priority_networks']}


storage_root_path = /opt/udh/${service.serviceName}/data


sys_log_dir = /opt/udh/${service.serviceName}/log