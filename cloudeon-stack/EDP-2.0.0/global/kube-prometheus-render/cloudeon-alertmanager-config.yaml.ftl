<#--如果使用外部kube-prometheus-stack,则由global组件初始化，否则由内置的kube-prometheus组件初始化-->
<#if conf['global.monitor.type'] =='EXTERNAL_KUBE_PROMETHEUS'>
apiVersion: monitoring.coreos.com/v1alpha1
kind: AlertmanagerConfig
metadata:
  name: server-alertmanager-config
<#--这里的命名空间是要监控的命名空间，而非kube-prometheus所在的命名空间-->
  namespace: ${namespace}
  labels:
    alertmanagerConfig: "true"
spec:
  route:
    groupBy: ['alert']
    groupWait: 30s
    groupInterval: 5m
    repeatInterval: 1h
    receiver: 'web.hook'
  receivers:
  - name: 'web.hook'
    webhookConfigs:
    - url: '${cloudeonURL}/apiPre/alert/webhook'
      sendResolved: true
  inhibitRules:
    - sourceMatch:
        - name: alertLevel
          value: "异常级别"
      targetMatch:
        - name: alertLevel
          value: "告警级别"
      equal: ['alert', 'dev', 'instance']

</#if>
