apiVersion: v1
kind: ConfigMap
metadata:
  name: ${configmapName}
<#if labels??>
  labels:
    <#list labels?keys as key>
    ${key}: "${labels[key]}"
    </#list>
</#if>
data:
<#list fileStrMap?keys as key>
  ${key}: |
${fileStrMap[key]}
</#list>
