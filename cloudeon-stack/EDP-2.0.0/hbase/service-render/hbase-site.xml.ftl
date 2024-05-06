<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<#--Simple macro definition-->
<#macro property key value>
<property>
  <name>${key}</name>
  <value>${value}</value>
</property>
</#macro>


<configuration>


  <@property "hbase.cluster.distributed" "true"/>
<#assign hdfs=dependencies.HDFS />
<#if hdfs.serviceRoles['HDFS_NAMENODE']?size gt 1>
  <#assign fs_default_uri = "hdfs://" + hdfs.conf['nameservices']>
</#if>
  <@property "hbase.rootdir" "${fs_default_uri}/${service.serviceName}"/>
  <@property "hbase.local.dir" "/data/1"/>

  <#--handle dependent.zookeeper-->
  <#if dependencies.ZOOKEEPER??>
    <#assign zookeeper=dependencies.ZOOKEEPER quorum=[]>
    <#list zookeeper.serviceRoles['ZOOKEEPER_SERVER'] as role>
      <#assign quorum += [role.hostname + ":" + zookeeper.conf["zookeeper.client.port"]]>
    </#list>
    <@property "hbase.zookeeper.quorum" quorum?join(",")/>
    <@property "zookeeper.znode.parent" "/${service.serviceName}"  />
  </#if>


  <#list confFiles['hbase-site.xml'] as key, value>
    <@property key value/>
  </#list>
</configuration>
