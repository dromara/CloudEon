<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<#--Simple macro definition-->
<#macro property key value>
<property>
    <name>${key}</name>
    <value>${value}</value>
</property>
</#macro>
<configuration>

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
