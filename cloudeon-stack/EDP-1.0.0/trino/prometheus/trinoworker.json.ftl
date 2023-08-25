[
{
"targets":[<#list serviceRoles['TRINO_WORKER'] as item>"${item.hostname}:8099"<#sep>,</#list>]
}
]
