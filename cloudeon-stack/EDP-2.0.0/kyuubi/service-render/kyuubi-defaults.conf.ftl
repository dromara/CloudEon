
## Kyuubi Configurations

# kyuubi.authentication                    NONE

kyuubi.frontend.bind.host                ${HOSTNAME}
kyuubi.frontend.protocols                     	THRIFT_BINARY,REST
kyuubi.engine.type                       SPARK_SQL


# 开启ha后，beeline访问方式 =>  beeline -u 'jdbc:hive2://test-1:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=kyuubi7' -n hive
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
spark.master                       yarn

<#if dependencies.SPARK??>
    <#assign spark=dependencies.SPARK >
# spark history
spark.history.fs.logDirectory    ${spark.conf['spark.history.fs.logDirectory']}
spark.eventLog.compress: false
spark.eventLog.enabled           true
spark.eventLog.dir               ${spark.conf['spark.eventLog.dir']}
spark.yarn.historyServer.address   ${spark.serviceRoles['SPARK_HISTORY_SERVER'][0].hostname}:${spark.conf['spark.history.ui.port']}
</#if>

<#if conf['plugin.iceberg'] =='true'>
spark.sql.extensions              org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions
spark.sql.catalog.iceberg_catalog           org.apache.iceberg.spark.SparkCatalog
spark.sql.catalog.iceberg_catalog.type        hadoop
spark.sql.catalog.iceberg_catalog.warehouse     ${conf['plugin.iceberg.warehouse']}
<#if conf['plugin.iceberg.as.defaultCatalog'] == 'true'>
spark.sql.defaultCatalog             iceberg_catalog
</#if>
</#if>

# Details in https://kyuubi.readthedocs.io/en/master/deployment/settings.html
<#list confFiles['kyuubi-defaults.conf']?keys as key>
${key}                            ${confFiles['kyuubi-defaults.conf'][key]}
</#list>
