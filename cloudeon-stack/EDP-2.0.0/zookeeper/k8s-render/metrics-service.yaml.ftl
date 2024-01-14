kind: Service
apiVersion: v1
metadata:
  name: metrics-${serviceFullName}
  labels:
    sname: ${serviceFullName}
    roleFullName: zookeeper-server
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
  ports:
  - name: metrics
    port: ${conf['zookeeper.metrics.port']}