<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<#--Simple macro definition-->
<#macro property key value>
<property>
    <name>${key}</name>
    <value>${value}</value>
</property>
</#macro>
<#--------------------------->
<configuration>
    <#if service.roles.YARN_HISTORYSERVER??>
    <#assign historyServer=service.roles.YARN_HISTORYSERVER[0]['hostname']>
    <@property "mapreduce.jobhistory.address" historyServer + ":10020"/>
    <@property "mapreduce.jobhistory.webapp.address" historyServer + ":19888"/>
    </#if>

    <#if service.auth = "kerberos">
    <@property "mapreduce.jobhistory.principal" "mapred/_HOST@" + service.realm></@property>
    <@property "mapreduce.jobhistory.keytab" service.keytab></@property>
    <@property "mapreduce.jobhistory.webapp.spnego-keytab-file" service.keytab></@property>
    <@property "mapreduce.jobhistory.webapp.spnego-principal" "HTTP/_HOST@" + service.realm></@property>
    </#if>
<#--Take properties from the context-->
<#list service['mapred_site'] as key, value>
    <@property key value/>
</#list>
</configuration>
