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
<#macro property key value>
    ${key}: "${value}"
</#macro>
<#list confFiles['kube-prometheus-stack.conf'] as key, value>
    <@property key value/>
</#list>
  valuesContent: |-
    namespaceOverride: "${conf['kube-prometheus.namespace']}"
    prometheus:
      prometheusSpec:
<#--添加自定义抓取配置，用于抓取注解形式的指标-->
        additionalScrapeConfigsSecret:
          enabled: true
          name: "prometheus-additional-scrape-configs"
          key: "prometheus-additional.yaml"
<#--自动抓取所有命名空间的serviceMonitor、podMonitor、alertRule-->
        serviceMonitorSelectorNilUsesHelmValues: false
        podMonitorSelectorNilUsesHelmValues: false
        ruleSelectorNilUsesHelmValues: false
<#--不设置的话默认会屏蔽掉一些指标，导致与prometheus-operator/kube-prometheus的不一样-->
    kubelet:
      serviceMonitor:
        cAdvisorMetricRelabelings: ""
    grafana:
      sidecar:
        dashboards:
          provider:
            allowUiUpdates: true
          folderAnnotation: "folder"
      grafana.ini:
        analytics:
          check_for_updates : false
        auth.anonymous:
          enabled : true
        security:
          allow_embedding: true
    alertmanager:
      alertmanagerSpec:
        alertmanagerConfigMatcherStrategy:
          type: None
        alertmanagerConfigSelector:
          matchLabels:
            alertmanagerConfig: "true"