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
    fsid=dependencies.HDFS!sid
    federation=service.nameservices?? && service.nameservices?size gt 0
>
<configuration>
<#assign fs_default_uri = "hdfs://" + fsid >
<#if federation>
    <#if service.nameservices?size == 1>
        <#assign fs_default_uri = "hdfs://" + service.nameservices[0]>
    </#if>
<#else>
    <#assign
        namenode=service.roles['HDFS_NAMENODE'][0].hostname
        namenodeport=service['namenode.rpc-port']
        fs_default_uri = "hdfs://" + namenode + ":" + namenodeport
    >
</#if>
<@property "fs.defaultFS" fs_default_uri/>

<#--handle dependent.zookeeper-->
<#if dependencies.ZOOKEEPER??>
    <#assign zookeeper=dependencies.ZOOKEEPER quorum=[]>
    <#list zookeeper.roles.ZOOKEEPER as role>
        <#assign quorum += [role.hostname + ":" + zookeeper["zookeeper.client.port"]]>
    </#list>
    <@property "ha.zookeeper.quorum" quorum?join(",")/>
    <@property "ha.zookeeper.parent-znode" "/" + sid + "-ha"/>
</#if>

<#--hadoop.proxyuser.[hive, hue, httpfs, oozie].[hosts,groups]-->
<#assign services=["hdfs","hbase","hive", "hue", "httpfs", "oozie", "guardian"]>
<#list services as s>
    <@property "hadoop.proxyuser." + s + ".hosts" "*"/>
    <@property "hadoop.proxyuser." + s + ".groups" "*"/>
</#list>
<#assign user=.data_model['current.user']>
<@property "hadoop.proxyuser." + user + ".hosts" "*"/>
<@property "hadoop.proxyuser." + user + ".groups" "*"/>
<@property "net.topology.node.switch.mapping.impl" "org.apache.hadoop.net.ScriptBasedMapping"/>
<@property "net.topology.script.file.name" "/usr/lib/transwarp/scripts/rack_map.sh"/>
<#--Take properties from the context-->
<#list service['core-site.xml'] as key, value>
    <@property key value/>
</#list>
</configuration>
