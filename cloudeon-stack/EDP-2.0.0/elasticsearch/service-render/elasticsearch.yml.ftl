<#if conf["data.path.list"]??&& conf["data.path.list"]?trim?has_content>
    <#assign dataPathListSize=conf["data.path.list"]?trim?split(",")?size>
<#else >
    <#assign dataPathListSize=1>
</#if>
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
cluster.name: elasticsearch
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
node.name: ${NODE_NAME}
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
#
# Path to log files:
#
path:
    logs: /workspace/logs
    data:
<#list 1..dataPathListSize as dataPathIndex>
        - /data/${dataPathIndex}
</#list>
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
bootstrap.memory_lock: false
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# By default Elasticsearch is only accessible on localhost. Set a different
# address here to expose this node on the network:
#
network.host: 0.0.0.0
#
# By default Elasticsearch listens for HTTP traffic on the first free port it
# finds starting at 9200. Set a specific HTTP port here:
#
http.port: ${conf['elasticsearch.http.listeners.port']}

transport.tcp.port: ${conf['elasticsearch.tcp.listeners.port']}

<#if serviceRoles['ELASTICSEARCH_NODE']?size gt 1>
    <#assign esNodes=[]>
    <#list serviceRoles['ELASTICSEARCH_NODE'] as role>
        <#assign esNodes += ["'"+role.hostname + "'"]>
    </#list>
discovery.seed_hosts: [${esNodes?join(",")}]
cluster.initial_master_nodes: [${esNodes?join(",")}]
<#else>
discovery.seed_hosts: ['${serviceRoles['ELASTICSEARCH_NODE'][0].hostname}']
cluster.initial_master_nodes: ['${serviceRoles['ELASTICSEARCH_NODE'][0].hostname}']
</#if>

#
# For more information, consult the discovery and cluster formation module documentation.
#
# --------------------------------- Readiness ----------------------------------
#
# Enable an unauthenticated TCP readiness endpoint on localhost
#
#readiness.port: 9399
#
# ---------------------------------- Various -----------------------------------
#
# Allow wildcard deletion of indices:
#
#action.destructive_requires_name: false

bootstrap.system_call_filter: false
ingest.geoip.downloader.enabled: false

xpack.security.enabled: false

#http.cors.enabled: true
#http.cors.allow-origin: "*"