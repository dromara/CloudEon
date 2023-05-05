[
{
"targets":[<#list serviceRoles['KYUUBI_SERVER'] as item>"${item.hostname}:${conf['kyuubi.metrics.prometheus.port']}"<#sep>,</#list>]
}
]
