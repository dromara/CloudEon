[
{
"targets":[<#list serviceRoles['DINKY_SERVER'] as item>"${item.hostname}:${conf['dinky.server.port']}"<#sep>,</#list>]
}
]