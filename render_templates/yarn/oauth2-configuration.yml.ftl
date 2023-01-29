<#if service.auth == "kerberos">
    <#if dependencies.GUARDIAN?? && dependencies.GUARDIAN.roles["GUARDIAN_FEDERATION"]??>
        <#assign guardian=dependencies.GUARDIAN federationTenant=guardian['guardian.ds.realm']>
---
oauth2:
  server:
    baseUrls: [<#list guardian.roles["GUARDIAN_FEDERATION"] as role><#assign url = "https://" + role.hostname + ":" + guardian["federation.server.port"] + "/federation-server">"${url}"<#sep>, </#list>]
<#if !guardian['guardian.server.authentication.oauth2.server.baseUrls.external']?matches("^\\s*$")>
    externalBaseUrls: [<#list guardian['guardian.server.authentication.oauth2.server.baseUrls.external']?split(",") as url>"${url}"<#sep>, </#list>]
</#if>
    endpoint:
      validation: "/${federationTenant}/oauth/check_token"
      authCode: "/${federationTenant}/oauth/authorize"
      token: "/${federationTenant}/oauth/token"
      logout: "/logout"
  client:
    credentialFile: "/etc/${service.sid}/conf/client-credential.jks"
    </#if>
</#if>