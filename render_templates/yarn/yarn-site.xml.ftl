<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<#--Simple macro definition-->
<#macro property key value>
<property>
    <name>${key}</name>
    <value>${value}</value>
</property>
</#macro>
<#--------------------------->
<#assign
    sid=service.sid
    auth=service.auth
    rm_port=service['resourcemanager.port']
    rm_track_port=service['resourcemanager.resource-tracker.port']
    rm_scheduler_port=service['resourcemanager.scheduler.port']
    rm_admin_port=service['resourcemanager.admin.port']
    rm_webapp_port=service['resourcemanager.webapp.port']
>
<configuration>

<#if service.auth = "kerberos">
    <@property "yarn.resourcemanager.keytab" service.keytab/>
    <@property "yarn.nodemanager.keytab" service.keytab/>
    <@property "yarn.resourcemanager.principal" "yarn/_HOST@" + service.realm/>
    <@property "yarn.nodemanager.principal" "yarn/_HOST@" + service.realm/>
    <@property "yarn.nodemanager.container-executor.class" "org.apache.hadoop.yarn.server.nodemanager.DefaultContainerExecutor"/>
    <@property "yarn.nodemanager.linux-container-executor.group" "yarn"/>
    <#if service.roles.YARN_TIMELINESERVER??>
    <@property "yarn.timeline-service.keytab" service.keytab/>
    <@property "yarn.timeline-service.principal" "yarn/_HOST@" + service.realm/>
    <@property "yarn.timeline-service.http-authentication.type" "kerberos"/>
    <@property "yarn.timeline-service.http-authentication.kerberos.principal" "HTTP/_HOST@" + service.realm/>
    <@property "yarn.timeline-service.http-authentication.kerberos.keytab" service.keytab/>
    </#if>
</#if>
<#if service.plugins?seq_contains("guardian")>
    <@property "yarn.service.id" service.sid/>
    <@property "yarn.authorization-provider" "io.transwarp.guardian.plugins.yarn.GuardianYarnAuthorizer"/>
    <@property "yarn.resourcemanager.configuration.provider-class" "io.transwarp.guardian.plugins.yarn.GuardianYarnConfigurationProvider"/>
</#if>

<#if service.roles.YARN_RESOURCEMANAGER?? && service.roles.YARN_RESOURCEMANAGER?size gt 1>
    <@property "yarn.resourcemanager.ha.enabled" "true"/>
    <@property "yarn.resourcemanager.recovery.enabled" "true"/>
    <@property "yarn.resourcemanager.store.class" "org.apache.hadoop.yarn.server.resourcemanager.recovery.ZKRMStateStore"/>
    <#--handle dependent.zookeeper-->
    <#if dependencies.ZOOKEEPER??>
        <#assign zookeeper=dependencies.ZOOKEEPER quorums=[]>
        <#list zookeeper.roles.ZOOKEEPER as role>
            <#assign quorums += [role.hostname + ":" + zookeeper["zookeeper.client.port"]]>
        </#list>
        <#assign quorum = quorums?join(",")>
    </#if>
    <@property "yarn.resourcemanager.zk-address" quorum/>
    <@property "yarn.resourcemanager.ha.automatic-failover.enabled" "true"/>
    <@property "yarn.resourcemanager.ha.automatic-failover.embedded" "true"/>
    <@property "yarn.resourcemanager.cluster-id" sid + "-cluster"/>
    <@property "yarn.resourcemanager.zk-state-store.parent-path" "/rmstore-" + sid/>
    <#assign  size=service.roles.YARN_RESOURCEMANAGER?size rmIds=[]>
    <#list 0..size-1 as i>
        <#assign rmId = "rm" + (i + 1)>
        <#assign rmIds += [rmId]>
        <#assign rm = service.roles.YARN_RESOURCEMANAGER[i]['hostname']>
        <@property "yarn.resourcemanager.address." + rmId rm + ":" + rm_port/>
        <@property "yarn.resourcemanager.resource-tracker.address." + rmId rm + ":" + rm_track_port/>
        <@property "yarn.resourcemanager.scheduler.address." + rmId rm + ":" + rm_scheduler_port/>
        <@property "yarn.resourcemanager.admin.address." + rmId rm + ":" + rm_admin_port/>
        <@property "yarn.resourcemanager.webapp.address." + rmId rm + ":" + rm_webapp_port/>
        <#if rm==localhostname>
            <@property "yarn.resourcemanager.ha.id" rmId/>
        </#if>
    </#list>
    <#assign rm_Ids = rmIds?join(",")>
    <@property "yarn.resourcemanager.ha.rm-ids" rm_Ids/>
<#else>
    <#assign resourceManager=service.roles.YARN_RESOURCEMANAGER[0]['hostname']>
    <@property "yarn.resourcemanager.address" resourceManager + ":" + rm_port/>
    <@property "yarn.resourcemanager.resource-tracker.address" resourceManager + ":" + rm_track_port/>
    <@property "yarn.resourcemanager.scheduler.address" resourceManager + ":" + rm_scheduler_port/>
    <@property "yarn.resourcemanager.admin.address" resourceManager + ":" + rm_admin_port/>
    <@property "yarn.resourcemanager.webapp.address" resourceManager + ":" + rm_webapp_port/>
</#if>

<#if service.roles.YARN_HISTORYSERVER?? && service.roles.YARN_HISTORYSERVER?size gt 0>
    <#assign historyServer=service.roles.YARN_HISTORYSERVER[0]['hostname']>
    <@property "yarn.log.server.url" "http://" + historyServer + ":19888/jobhistory/logs/"/>
</#if>

<#if service.roles.YARN_TIMELINESERVER?? && service.roles.YARN_TIMELINESERVER?size gt 0>
    <#assign timelineServer=service.roles.YARN_TIMELINESERVER[0]['hostname']>
    <@property "yarn.timeline-service.hostname" timelineServer/>
    <@property "yarn.timeline-service.webapp.https.address" timelineServer + ":8190"/>
    <@property "yarn.timeline-service.webapp.address" timelineServer + ":8188"/>
</#if>

    <@property "yarn.resourcemanager.nodes.exclude-path" "/etc/" + sid + "/conf/yarn.exclude"/>
<#--Take properties from the context-->
<#list confFiles['yarn-site.xml'] as key, value>
    <@property key value/>
</#list>
</configuration>
