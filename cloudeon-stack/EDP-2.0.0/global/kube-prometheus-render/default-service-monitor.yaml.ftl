<#--如果使用外部kube-prometheus-stack,则由global组件初始化，否则由内置的kube-prometheus组件初始化-->
<#if conf['global.monitor.type'] =='EXTERNAL_KUBE_PROMETHEUS'>
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: default-service-monitor
  labels:
    team: frontend
spec:
  selector:
    matchLabels:
      enable-default-service-monitor: "true"
  endpoints:
  - port: metrics
</#if>