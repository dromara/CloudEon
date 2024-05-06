<#macro executeDynamicCode userInput>
    <#assign startIdx = userInput?index_of('#{{')>
    <#assign endIdx = userInput?last_index_of('}}#')>
    <#if startIdx gt 0 && endIdx gt 0>
        <#assign dynamicCode = userInput?substring(startIdx + 3, endIdx)>
        ${userInput?substring(0,startIdx)}<@dynamicCode?interpret />${userInput?substring(endIdx+3)}<#t>
    <#else >
        ${userInput}
    </#if><#t>
</#macro>
<#if alertRules ?? && alertRules?size gt 0>
apiVersion: monitoring.coreos.com/v1
kind: PrometheusRule
metadata:
  name: ${serviceFullName}
spec:
  groups:
    - name: ${serviceFullName}
      rules:
      <#list alertRules as rule>
      -  alert: ${rule.ruleName}
         expr: <@executeDynamicCode rule.promql />
         labels:
             receiver: "webhook"
             alertLevel: "${rule.alertLevel}"
             clusterId: "${rule.clusterId}"
             serviceRoleName: "${rule.stackRoleName}"
             serviceName: "${rule.stackServiceName}"
         annotations:
             alertAdvice: "${rule.alertAdvice}"
             alertInfo:  "${rule.alertInfo}"
      </#list>
</#if>