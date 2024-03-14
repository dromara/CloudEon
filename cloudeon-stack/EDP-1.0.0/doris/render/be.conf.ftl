<#list confFiles['be.conf']?keys as key>
    ${key}=${confFiles['be.conf'][key]}
</#list>
PPROF_TMPDIR="/opt/edp/${service.serviceName}/log"

# since 1.2, the JAVA_HOME need to be set to run BE process.
# JAVA_HOME=/path/to/jdk/

JAVA_HOME=/opt/jdk1.8.0_141/

priority_networks = ${localhostip}/24


storage_root_path = /opt/edp/${service.serviceName}/data


sys_log_dir = /opt/edp/${service.serviceName}/log