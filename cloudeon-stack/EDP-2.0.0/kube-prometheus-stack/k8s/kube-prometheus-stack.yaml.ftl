apiVersion: helm.cattle.io/v1
kind: HelmChart
metadata:
  name: ${roleServiceFullName}
  namespace: ${conf['kube-prometheus.namespace']}
spec:
  repo: ${conf['kube-prometheus.helm.repo']}
  chart: kube-prometheus-stack
  targetNamespace: ${conf['kube-prometheus.namespace']}
  set:
    namespaceOverride: "${conf['kube-prometheus.namespace']}"
<#--添加自定义抓取配置，用于抓取注解形式的指标-->
    prometheus.prometheusSpec.additionalScrapeConfigsSecret.enabled: "true"
    prometheus.prometheusSpec.additionalScrapeConfigsSecret.name: "prometheus-additional-scrape-configs"
    prometheus.prometheusSpec.additionalScrapeConfigsSecret.key: "prometheus-additional.yaml"
<#--自动抓取所有命名空间的serviceMonitor和podMonitor-->
    prometheus.prometheusSpec.serviceMonitorSelectorNilUsesHelmValues: "false"
    prometheus.prometheusSpec.podMonitorSelectorNilUsesHelmValues: "false"
<#--不设置的话默认会屏蔽掉一些指标-->
    kubelet.serviceMonitor.cAdvisorMetricRelabelings: ""
    grafana.sidecar.dashboards.folderAnnotation: "folder"
    grafana.sidecar.dashboards.provider.allowUiUpdates: "true"
    grafana.grafana\.ini.security.allow_embedding: "true"
    grafana.grafana\.ini.auth\.anonymous.enabled : "true"
    grafana.grafana\.ini.analytics.check_for_updates : "false"
<#macro property key value>
    ${key}: "${value}"
</#macro>
<#list confFiles['kube-prometheus-stack.conf'] as key, value>
    <@property key value/>
</#list>
