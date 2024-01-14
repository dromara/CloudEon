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
