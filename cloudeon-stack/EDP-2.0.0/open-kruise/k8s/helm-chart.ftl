apiVersion: helm.cattle.io/v1
kind: HelmChart
metadata:
  name: ${roleServiceFullName}
spec:
  repo: ${conf['kruise.helm.repo']}
  chart: kruise
  set:
    installation.namespace: "${conf['installation.namespace']}"
    installation.createNamespace: "true"
    manager.hostNetwork: "true"
    featureGates: "SidecarTerminator=true\\,ImagePullJobGate=true"
<#macro property key value>
    ${key}: "${value}"
</#macro>
<#list confFiles['kruise.conf'] as key, value>
    <@property key value/>
</#list>
