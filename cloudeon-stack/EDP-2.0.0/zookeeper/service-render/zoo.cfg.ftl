<#compress>
dataDir=/data/1
clientPort=${conf['zookeeper.client.port']}
admin.enableServer=false
metricsProvider.className=org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider
metricsProvider.httpPort=${conf['zookeeper.metrics.port']}
<#--Generate all server properties-->
<#assign roles=serviceRoles['ZOOKEEPER_SERVER'] size=roles?size>
<#list 1..size as n>
    <#assign role=roles[n_index]
    commu_port=conf['zookeeper.peer.communicate.port']
    elect_port=conf['zookeeper.leader.elect.port']>
    server.${role.id % 254 + 1}=${role.hostname}:${commu_port}:${elect_port}
</#list>


    skipACL=yes


<#--Take properties from the context-->
<#list confFiles['zoo.cfg']?keys as key>
    ${key}=${confFiles['zoo.cfg'][key]}
</#list>

autopurge.purgeInterval=1
autopurge.snapRetainCount=10
</#compress>
