<#if service.roles.YARN_NODEMANAGER?? && service.roles.YARN_NODEMANAGER?size gt 0>
  <#list service.roles.YARN_NODEMANAGER as nodemanager>
    <#if nodemanager.toDecommission?? && nodemanager.toDecommission=true>
${nodemanager.hostname}
    </#if>
  </#list>
</#if>
