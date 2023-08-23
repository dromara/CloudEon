[
{
"targets":[<#list serviceRoles['TRINO_COORDINATOR'] as item>"${item.hostname}:${conf['coordinator.http.port']}"<#sep>,</#list>]
}
]
