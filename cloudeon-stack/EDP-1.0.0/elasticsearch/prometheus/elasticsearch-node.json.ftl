[
{
"targets":[<#list serviceRoles['ELASTICSEARCH_NODE'] as item>"${item.hostname}:5556"<#sep>,</#list>]
}
]