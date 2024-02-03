apiVersion: v1
kind: ConfigMap
metadata:
  labels:
    name: hdfs-config
  name: hdfs-config
data:
  core-site.xml: |
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
        serviceName=service.serviceName
    >
    <configuration>
    <#if serviceRoles['HDFS_NAMENODE']?size gt 1>
        <#assign fs_default_uri = "hdfs://" + conf['nameservices']>
    <#else >
        <#assign
        namenode=serviceRoles['HDFS_NAMENODE'][0].hostname
        namenodeport=conf['namenode.rpc-port']
        fs_default_uri = "hdfs://" + namenode + ":" + namenodeport
        >
    </#if>

    <@property "fs.defaultFS" fs_default_uri/>

    <#--handle dependent.zookeeper-->
    <#if dependencies.ZOOKEEPER??>
        <#assign zookeeper=dependencies.ZOOKEEPER quorum=[]>
        <#list zookeeper.serviceRoles['ZOOKEEPER_SERVER'] as role>
            <#assign quorum += [role.hostname + ":" + zookeeper.conf["zookeeper.client.port"]]>
        </#list>
        <@property "ha.zookeeper.quorum" quorum?join(",")/>
        <@property "ha.zookeeper.parent-znode" "/" + serviceName + "-ha"/>
    </#if>

    <#--hadoop.proxyuser.[hive, hue, httpfs, oozie].[hosts,groups]-->
    <#assign services=["root","yarn","hadoop","spark","zookeeper","kyuubi","flink","hdfs","hbase","hive", "hue", "httpfs"]>
    <#list services as s>
        <@property "hadoop.proxyuser." + s + ".hosts" "*"/>
        <@property "hadoop.proxyuser." + s + ".groups" "*"/>
    </#list>
    <#--<@property "net.topology.node.switch.mapping.impl" "org.apache.hadoop.net.ScriptBasedMapping"/>-->
    <#--<@property "net.topology.script.file.name" "/opt/rack_map.sh"/>-->
    <#--Take properties from the context-->
    <#list confFiles['core-site.xml'] as key, value>
        <@property key value/>
    </#list>
    </configuration>

  hdfs-site.xml: |
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

<#--对外暴露的yarn-site.xml文件不需要提供存储位置-->
    <#--handle data dir-->
<#--    <@property "dfs.datanode.data.dir" "/workspace/data/datanode"/>-->
<#--    <@property "dfs.namenode.name.dir" "/workspace/data/namenode"/>-->
<#--    <@property "dfs.journalnode.edits.dir" "/workspace/data/journal"/>-->

    <#--handle journalnode-->
    <#assign useWildcard=conf['journalnode.use.wildcard']
             rpcPort=conf['journalnode.rpc-port']
             httpPort=conf['journalnode.http-port']
             hostname="0.0.0.0"/>
        <@property "dfs.journalnode.rpc-address" hostname + ":" + rpcPort/>
        <@property "dfs.journalnode.http-address" hostname + ":" + httpPort/>
    <#--handleDatanode-->
    <#assign useWildcard=conf["datanode.use.wildcard"]
             hostname="0.0.0.0"/>
        <@property "dfs.datanode.address" hostname + ":" + conf["datanode.port"]/>
        <@property "dfs.datanode.http.address" hostname + ":" + conf["datanode.http-port"]/>
        <@property "dfs.datanode.ipc.address" hostname + ":" + conf["datanode.ipc-port"]/>
    <#--handleOther-->
        <@property "dfs.hosts.exclude" "/opt/hadoop/etc/hadoop/dfs.exclude"/>

    <#--    dn_socket需要其父目录权限为755，否则会报错   -->
    <#--    <@property "dfs.domain.socket.path" "/opt/edp/${serviceName}/data/dn_socket"/>-->

    <#--Take properties from the context-->
    <#list confFiles['hdfs-site.xml'] as key, value>
        <@property key value/>
    </#list>
    </configuration>
