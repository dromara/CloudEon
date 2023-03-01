<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<#--Simple macro definition-->
<#macro property key value>
<property>
    <name>${key}</name>
    <value>${value}</value>
</property>
</#macro>
<#--------------------------->
<#assign sid=service.sid auth=service.auth realm=service.realm>
<configuration>

<#--handle federation-->
<#assign namenode_use_wildcard=service['namenode.use.wildcard']>
<#--federation is true-->
<#if service.nameservices?? && service.nameservices?size gt 0>
    <@property "dfs.nameservices" service.nameservices?join(",")/>
<#list service.nameservices as ns>
<#--ha is enabled-->
    <#if service[ns]['HDFS_NAMENODE']?size gt 1>
    <@property "dfs.ha.namenodes." + ns "nn1,nn2"/>
    <#assign
        nn1=service[ns]['HDFS_NAMENODE'][0].hostname
        nn2=service[ns]['HDFS_NAMENODE'][1].hostname
        nn_rpc_port=service['namenode.rpc-port']
        nn_http_port=service['namenode.http-port']
    >
    <@property "dfs.namenode.rpc-address." + ns + ".nn1" nn1 + ":" + nn_rpc_port/>
    <@property "dfs.namenode.rpc-address." + ns + ".nn2" nn2 + ":" + nn_rpc_port/>
    <@property "dfs.namenode.http-address." + ns + ".nn1" nn1 + ":" + nn_http_port/>
    <@property "dfs.namenode.http-address." + ns + ".nn2" nn2 + ":" + nn_http_port/>
    <@property "dfs.ha.automatic-failover.enabled." + ns "true"/>
    <#assign
        default_jn_rpc_port=service['journalnode.rpc-port']
        journalnodes=service[ns]['HDFS_JOURNALNODE']
        jn_withport=[]
    >
    <#list journalnodes as jn>
        <#assign jn_withport += [(jn.hostname + ":" + service['journalnode.rpc-port']!default_jn_rpc_port)]>
    </#list>
    <@property "dfs.namenode.shared.edits.dir." + ns "qjournal://" + jn_withport?join(";") + "/" + ns/>
    <#else>
    <#assign nn=service[ns]['HDFS_NAMENODE'][0].hostname
             nn_rpc_port=service['namenode.rpc-port']
             nn_http_port=service['namenode.http-port']>
    <@property "dfs.namenode.rpc-address." + ns nn_rpc_port/>
    <@property "dfs.namenode.http-address." + ns nn_http_port/>
    </#if>
    <@property "dfs.client.failover.proxy.provider." + ns "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"/>
    <@property "dfs.ha.automatic-failover.enabled." + ns "true"/>
</#list>
<#else>
<#--federation is false-->
    <@property "dfs.namenode.rpc-address" service.roles['HDFS_NAMENODE'][0].hostname + ":" + default_nn_rpc_port/>
    <#assign namenode=(namenode_use_wildcard == "true")?string("0.0.0.0", service.roles['HDFS_NAMENODE'][0].hostname)>
    <@property "dfs.namenode.http-address"  namenode + default_nn_http_port/>
</#if>
<#--handle journalnode-->
<#assign useWildcard=service['journalnode.use.wildcard']
         rpcPort=service['journalnode.rpc-port']
         httpPort=service['journalnode.http-port']
         hostname=(useWildcard == "true")?string("0.0.0.0", localhostname)/>
    <@property "dfs.journalnode.rpc-address" hostname + ":" + rpcPort/>
    <@property "dfs.journalnode.http-address" hostname + ":" + httpPort/>
<#--handleDatanode-->
<#assign useWildcard=service["datanode.use.wildcard"]
         hostname=(useWildcard == "true")?string("0.0.0.0", localhostname)/>
    <@property "dfs.datanode.address" hostname + ":" + service["datanode.port"]/>
    <@property "dfs.datanode.http.address" hostname + ":" + service["datanode.http-port"]/>
    <@property "dfs.datanode.ipc.address" hostname + ":" + service["datanode.ipc-port"]/>
<#--handleOther-->
    <@property "dfs.hosts.exclude" "/etc/${sid}/conf/exclude-list.txt"/>
    <@property "dfs.domain.socket.path" "/var/run/${sid}/dn_socket"/>
    <#if dependencies.LICENSE_SERVICE??>
    <#assign  license=dependencies.LICENSE_SERVICE license_servers=[]>
    <#list license.roles["LICENSE_NODE"] as role>
        <#assign license_servers += [(role.hostname + ":" + license[role.hostname]["zookeeper.client.port"])]>
    </#list>
    <@property "license.zookeeper.quorum" license_servers?join(",")/>
    </#if>
    <@property "dfs.journalnode.edits.dir" "/hadoop/journal"/>
<#--Take properties from the context-->
<#list service['hdfs-site.xml'] as key, value>
    <@property key value/>
</#list>
</configuration>
