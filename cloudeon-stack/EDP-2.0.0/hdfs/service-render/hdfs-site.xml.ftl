<#if conf["data.path.list"]??&& conf["data.path.list"]?trim?has_content>
    <#assign dataPathListSize=conf["data.path.list"]?trim?split(",")?size>
<#else >
    <#assign dataPathListSize=1>
</#if>
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<#--Simple macro definition-->
<#macro property key value>
<property>
    <name>${key}</name>
    <value>${value}</value>
</property>
</#macro>
<#--------------------------->
<#assign serviceName=service.serviceName  >
<configuration>

<#assign  ns=conf['nameservices']>

<@property "dfs.nameservices" ns/>

<#--ha is enabled-->
<#if serviceRoles['HDFS_NAMENODE']?size gt 1>
    <@property "dfs.ha.namenodes." + ns "nn1,nn2"/>
    <#assign
        nn1=serviceRoles['HDFS_NAMENODE'][0].hostname
        nn2=serviceRoles['HDFS_NAMENODE'][1].hostname
        nn_rpc_port=conf['namenode.rpc-port']
        nn_http_port=conf['namenode.http-port']
    >
    <@property "dfs.namenode.rpc-address." + ns + ".nn1" nn1 + ":" + nn_rpc_port/>
    <@property "dfs.namenode.rpc-address." + ns + ".nn2" nn2 + ":" + nn_rpc_port/>
    <@property "dfs.namenode.http-address." + ns + ".nn1" nn1 + ":" + nn_http_port/>
    <@property "dfs.namenode.http-address." + ns + ".nn2" nn2 + ":" + nn_http_port/>
    <#assign
        default_jn_rpc_port=conf['journalnode.rpc-port']
        journalnodes=serviceRoles['HDFS_JOURNALNODE']
        jn_withport=[]
    >
    <#list journalnodes as jn>
        <#assign jn_withport += [(jn.hostname + ":" + conf['journalnode.rpc-port']!default_jn_rpc_port)]>
    </#list>
    <@property "dfs.namenode.shared.edits.dir"  "qjournal://" + jn_withport?join(";") + "/" + ns/>

    <@property "dfs.client.failover.proxy.provider." + ns "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"/>
    <@property "dfs.ha.automatic-failover.enabled"  "true"/>
<#else>
    <#assign nn=serviceRoles['HDFS_NAMENODE'][0].hostname
    nn_rpc_port=conf['namenode.rpc-port']
    nn_http_port=conf['namenode.http-port']>
    <@property "dfs.namenode.rpc-address." + ns nn_rpc_port/>
    <@property "dfs.namenode.http-address." + ns nn_http_port/>
</#if>

<#--handle data dir-->
<#--    datanode支持多路径存储-->
<#assign concatenatedPaths="">
<#list 1..dataPathListSize as dataPathIndex>
  <#assign concatenatedPaths = concatenatedPaths + "file:///data/${dataPathIndex}">
    <#if dataPathIndex < dataPathListSize>
        <#assign concatenatedPaths = concatenatedPaths + ",">
    </#if>
</#list>
    <@property "dfs.datanode.data.dir" concatenatedPaths/>
    <#--    namenode的持久化路径支持多个，但每个路径存储的数据是重复的，没必要写多个-->
    <@property "dfs.namenode.name.dir" "/data/1/namenode"/>
    <#--    journalnode的持久化路径只能有一个-->
    <@property "dfs.journalnode.edits.dir" "/data/1/journalnode"/>

<#--handle journalnode-->
<#assign useWildcard=conf['journalnode.use.wildcard']
         rpcPort=conf['journalnode.rpc-port']
         httpPort=conf['journalnode.http-port']
         hostname=(useWildcard == "true")?string("0.0.0.0", HOSTNAME)/>
    <@property "dfs.journalnode.rpc-address" hostname + ":" + rpcPort/>
    <@property "dfs.journalnode.http-address" hostname + ":" + httpPort/>
<#--handleDatanode-->
<#assign useWildcard=conf["datanode.use.wildcard"]
         hostname=(useWildcard == "true")?string("0.0.0.0", HOSTNAME)/>
    <@property "dfs.datanode.address" hostname + ":" + conf["datanode.port"]/>
    <@property "dfs.datanode.http.address" hostname + ":" + conf["datanode.http-port"]/>
    <@property "dfs.datanode.ipc.address" hostname + ":" + conf["datanode.ipc-port"]/>
<#--handleOther-->
    <@property "dfs.hosts.exclude" "${HADOOP_CONF_DIR}/dfs.exclude"/>

<#--    dn_socket需要其父目录权限为755，否则会报错   -->
<#--    <@property "dfs.domain.socket.path" "/opt/edp/${serviceName}/data/dn_socket"/>-->

<#--Take properties from the context-->
<#list confFiles['hdfs-site.xml'] as key, value>
    <@property key value/>
</#list>
</configuration>
