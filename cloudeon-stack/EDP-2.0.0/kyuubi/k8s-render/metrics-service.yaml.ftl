kind: Service
apiVersion: v1
metadata:
  name: metrics-kyuubi-server
  labels:
    sname: ${serviceFullName}
    roleFullName: kyuubi-server
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: kyuubi-server
  ports:
  - name: metrics
    port: ${conf['kyuubi.metrics.prometheus.port']}
