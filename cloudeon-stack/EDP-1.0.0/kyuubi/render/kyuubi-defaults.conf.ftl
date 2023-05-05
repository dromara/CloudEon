
## Kyuubi Configurations

# kyuubi.authentication                    NONE

kyuubi.frontend.bind.host                ${localhostname}
kyuubi.frontend.protocols                     	THRIFT_BINARY,REST
kyuubi.engine.type                       SPARK_SQL



<#if dependencies.ZOOKEEPER??>
    <#assign zookeeper=dependencies.ZOOKEEPER quorum=[]>
    <#list zookeeper.serviceRoles['ZOOKEEPER_SERVER'] as role>
        <#assign quorum += [role.hostname + ":" + zookeeper.conf["zookeeper.client.port"]]>
    </#list>
kyuubi.ha.addresses                      ${quorum?join(",")}
kyuubi.ha.namespace                      ${service.serviceName}
</#if>



kyuubi.metrics.reporters                   PROMETHEUS

## Spark Configurations
# 这里可以输入spark原生的配置，优先级会比spark-default.conf高
<#if dependencies.SPARK??>
    <#assign spark=dependencies.SPARK >
# spark history
spark.history.fs.logDirectory    ${spark.conf['spark.history.fs.logDirectory']}
spark.eventLog.compress: false
spark.eventLog.enabled           true
spark.eventLog.dir               ${spark.conf['spark.eventLog.dir']}
spark.yarn.historyServer.address   ${spark.serviceRoles['SPARK_HISTORY_SERVER'][0].hostname}:${spark.conf['spark.history.ui.port']}
</#if>

# Details in https://kyuubi.readthedocs.io/en/master/deployment/settings.html
<#list confFiles['kyuubi-defaults.conf']?keys as key>
${key}                            ${confFiles['kyuubi-defaults.conf'][key]}
</#list>
