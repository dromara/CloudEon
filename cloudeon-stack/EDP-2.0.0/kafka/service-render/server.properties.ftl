<#if conf["data.path.list"]??&& conf["data.path.list"]?trim?has_content>
    <#assign dataPathListSize=conf["data.path.list"]?trim?split(",")?size>
<#else >
    <#assign dataPathListSize=1>
</#if>

<#assign hosts=serviceRoles['KAFKA_BROKER']>
<#list hosts as host>
    <#if host.hostname == HOSTNAME>
broker.id=${host.id % 254 + 1}
    </#if>
</#list>

<#assign concatenatedPaths="">
<#list 1..dataPathListSize as dataPathIndex>
  <#assign concatenatedPaths = concatenatedPaths + "/data/${dataPathIndex}">
    <#if dataPathIndex < dataPathListSize>
        <#assign concatenatedPaths = concatenatedPaths + ",">
    </#if>
</#list>
log.dirs=${concatenatedPaths}

listeners=PLAINTEXT://0.0.0.0:${conf['kafka.listeners.port']}
advertised.listeners=PLAINTEXT://${HOSTNAME}:${conf['kafka.listeners.port']}

<#--handle dependent.zookeeper-->
<#assign zookeeper=dependencies.ZOOKEEPER quorum=[]>
<#list zookeeper.serviceRoles['ZOOKEEPER_SERVER'] as role>
<#assign quorum += [role.hostname + ":" + zookeeper.conf["zookeeper.client.port"]]>
</#list>
zookeeper.connect=${quorum?join(",")}/${service.serviceName}

<#list confFiles['server.properties']?keys as key>
${key}=${confFiles['server.properties'][key]}
</#list>
