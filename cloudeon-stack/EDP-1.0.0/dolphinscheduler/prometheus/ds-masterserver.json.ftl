[
{
"targets":[<#list serviceRoles['DS_MASTER_SERVER'] as item>"${item.hostname}:${conf['master.server.port']}/actuator/prometheus"<#sep>,</#list>]
}
]
