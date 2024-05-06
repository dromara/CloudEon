
# Default system properties included when running spark-submit.
# This is useful for setting default environmental settings.


<#macro property key value>
${key}   ${value}
</#macro>

<#list confFiles['spark-defaults.conf'] as key, value>
    <@property key value/>
</#list>

<@property "spark.yarn.historyServer.address"  "${HOSTNAME}:${conf['spark.history.ui.port']}" />


<#if conf['plugin.iceberg'] =='true'>
spark.sql.extensions              org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions
spark.sql.catalog.iceberg_catalog           org.apache.iceberg.spark.SparkCatalog
spark.sql.catalog.iceberg_catalog.type        hadoop
spark.sql.catalog.iceberg_catalog.warehouse     ${conf['plugin.iceberg.warehouse']}
    <#if conf['plugin.iceberg.as.defaultCatalog'] == 'true'>
spark.sql.defaultCatalog             iceberg_catalog
    </#if>
</#if>