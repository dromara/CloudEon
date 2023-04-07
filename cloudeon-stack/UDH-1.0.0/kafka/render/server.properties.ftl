


<#assign hosts=serviceRoles['KAFKA_BROKER']>
<#list hosts as host>
    <#if host.hostname == localhostname>
broker.id=${host.id % 254 + 1}
    </#if>
</#list>

log.dirs=/opt/udh/${service.serviceName}/data

listeners=PLAINTEXT://${localhostname}:${conf['kafka.listeners.port']}

<#--handle dependent.zookeeper-->
<#if dependencies.ZOOKEEPER??>
<#assign zookeeper=dependencies.ZOOKEEPER quorum=[]>
<#list zookeeper.serviceRoles['ZOOKEEPER_SERVER'] as role>
<#assign quorum += [role.hostname + ":" + zookeeper.conf["zookeeper.client.port"]]>
</#list>
zookeeper.connect=${quorum?join(",")}
</#if>

<#list confFiles['server.properties']?keys as key>
${key}=${confFiles['server.properties'][key]}
</#list>
