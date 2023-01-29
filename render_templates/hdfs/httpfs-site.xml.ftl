<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<#--Simple macro definition-->
<#macro property key value>
<property>
    <name>${key}</name>
    <value>${value}</value>
</property>
</#macro>
<configuration>
<#if service.auth = "kerberos">
    <#if dependencies.GUARDIAN??>
        <#--handle CAS-->
        <#if dependencies.GUARDIAN.roles.CAS_SERVER??>
            <#assign casServerSslPort=dependencies.GUARDIAN['cas.server.ssl.port']>
            <#if dependencies.GUARDIAN['guardian.server.cas.server.host']?matches("^\\s*$")>
                <#assign casServerName="https://${dependencies.GUARDIAN.roles.CAS_SERVER[0]['ip']}:${casServerSslPort}">
            <#else>
                <#assign casServerName="https://${dependencies.GUARDIAN['guardian.server.cas.server.host']}:${casServerSslPort}">
            </#if>
            <@property "dfs.httpfs.cas.enabled" "${service['dfs.httpfs.cas.enabled']}"/>
        </#if>
        <#--handle Guardian Federation-->
        <#if dependencies.GUARDIAN.roles["GUARDIAN_FEDERATION"]??>
            <@property "dfs.httpfs.oauth2.enabled" "${service['dfs.httpfs.oauth2.enabled']}"/>
        </#if>
    </#if>
</#if>
    <@property "httpfs.hadoop.config.dir" "/etc/${service.sid}/conf"/>
    <@property "httpfs.proxyuser.hue.groups" "*"/>
    <@property "httpfs.proxyuser.hue.hosts" "*"/>
    <@property "httpfs.proxyuser.root.groups" "*"/>
    <@property "httpfs.proxyuser.root.hosts" "*"/>
    <@property "httpfs.proxyuser.hdfs.groups" "*"/>
    <@property "httpfs.proxyuser.hdfs.hosts" "*"/>
    <@property "httpfs.proxyuser.hbase.groups" "*"/>
    <@property "httpfs.proxyuser.hbase.hosts" "*"/>
    <#if service.auth = "kerberos" >
    <@property "httpfs.authentication.type" "kerberos"/>
    <@property "httpfs.hadoop.authentication.type" "kerberos"/>
    <@property "httpfs.authentication.kerberos.principal" "HTTP/${localhostname?lower_case}@${service.realm}"/>
    <@property "httpfs.authentication.kerberos.keytab" "${service.keytab}"/>
    <@property "httpfs.hadoop.authentication.kerberos.principal" "httpfs/${localhostname?lower_case}@${service.realm}"/>
    <@property "httpfs.hadoop.authentication.kerberos.keytab" "${service.keytab}"/>
    </#if>
    <#if service['httpfs-site.xml']??>
        <#list service['httpfs-site.xml'] as key, value>
            <@property key value/>
        </#list>
    </#if>
</configuration>
