apiVersion: monitoring.coreos.com/v1alpha1
kind: AlertmanagerConfig
metadata:
  name: server-alertmanager-config
<#--这里的命名空间是要监控的命名空间，而非kube-prometheus所在的命名空间-->
  namespace: ${namespace}
  labels:
    alertmanagerConfig: "true"
spec:
<#-- 这个route将匹配所有当前命名空间的告警，所以需要将默认receiver设置为null，并设置子路由-->
  route:
    groupBy: ['alert']
    groupWait: 30s
    groupInterval: 1m
    repeatInterval: 2m
    receiver: 'web.hook'
    continue: true
    matchers:
      - name: receiver
        value: "webhook"
  receivers:
  - name: 'null'
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

