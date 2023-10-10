[
{
"targets":[<#list serviceRoles['ELASTICSEARCH_NODE'] as item>"${item.hostname}:${conf['elasticsearch.exporter.port']}"<#sep>,</#list>]
}
]