<#if service.roles.HDFS_DATANODE?? && service.roles.HDFS_DATANODE?size gt 0>
  <#list service.roles.HDFS_DATANODE as datanode>
    <#if datanode.toDecommission?? && datanode.toDecommission=true>
${datanode.hostname}
    </#if>
  </#list>
</#if>
