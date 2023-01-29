<#assign hosts=service['roles']['ZOOKEEPER']>
<#list hosts as host>
<#if host.hostname == localhostname>
${host.id % 254 + 1}
</#if>
</#list>
