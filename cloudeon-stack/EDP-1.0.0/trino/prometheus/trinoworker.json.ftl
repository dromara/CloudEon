[
{
"targets":[<#list serviceRoles['TRINO_WORKER'] as item>"${item.hostname}:${conf['worker.http.port']}"<#sep>,</#list>]
}
]
