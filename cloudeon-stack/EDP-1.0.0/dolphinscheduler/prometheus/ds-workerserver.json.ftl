[
{
"targets":[<#list serviceRoles['DS_WORKER_SERVER'] as item>"${item.hostname}:${conf['worker.server.port']}/actuator/prometheus"<#sep>,</#list>]
}
]
