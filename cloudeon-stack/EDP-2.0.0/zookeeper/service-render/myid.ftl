<#assign hosts=serviceRoles['ZOOKEEPER_SERVER']>
<#list hosts as host>
<#if host.hostname == HOSTNAME ||  host.hostname == NODE_NAME>
${host.id % 254 + 1}
</#if>
</#list>
