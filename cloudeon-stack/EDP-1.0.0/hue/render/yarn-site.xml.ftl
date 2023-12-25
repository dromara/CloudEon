<?xml version="1.0" encoding="utf-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

<#macro property key value>
    <property>
        <name>${key}</name>
        <value>${value}</value>
    </property>
</#macro>

<configuration>
    <#if dependencies.YARN??>
        <#assign yarn=dependencies.YARN>
        <#assign
        rm_port=yarn.conf['resourcemanager.port']
        rm_webapp_port=yarn.conf['resourcemanager.webapp.port']
        >

        <#if yarn.serviceRoles['YARN_RESOURCEMANAGER']?? && yarn.serviceRoles['YARN_RESOURCEMANAGER']?size gt 1>
            <@property "yarn.resourcemanager.ha.enabled" "true"/>
            <#if dependencies.ZOOKEEPER??>
                <#assign zookeeper=dependencies.ZOOKEEPER quorums=[]>
                <#list zookeeper.serviceRoles['ZOOKEEPER_SERVER'] as role>
                    <#assign quorums += [role.hostname + ":" + zookeeper.conf["zookeeper.client.port"]]>
                </#list>
                <#assign quorum = quorums?join(",")>
            </#if>
            <@property "yarn.resourcemanager.zk-address" quorum/>
            <@property "yarn.resourcemanager.cluster-id" yarn.service.serviceName + "-cluster"/>
            <#assign  size=yarn.serviceRoles['YARN_RESOURCEMANAGER']?size rmIds=[]>
            <#list 0..size-1 as i>
                <#assign rmId = "rm" + (i + 1)>
                <#assign rmIds += [rmId]>
                <#assign rm = yarn.serviceRoles['YARN_RESOURCEMANAGER'][i]['hostname']>
                <@property "yarn.resourcemanager.address." + rmId rm + ":" + rm_port/>
                <@property "yarn.resourcemanager.webapp.address." + rmId rm + ":" + rm_webapp_port/>
            </#list>
            <#assign rm_Ids = rmIds?join(",")>
            <@property "yarn.resourcemanager.ha.rm-ids" rm_Ids/>
        <#else>
            <#assign resourceManager=yarn.serviceRoles['YARN_RESOURCEMANAGER'][0]['hostname']>
            <@property "yarn.resourcemanager.address" resourceManager + ":" + rm_port/>
            <@property "yarn.resourcemanager.webapp.address" resourceManager + ":" + rm_webapp_port/>
        </#if>

    </#if>

</configuration>

