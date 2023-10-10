kind: Service
apiVersion: v1
metadata:
  name: metrics-zookeeper-server
  labels:
    sname: ${serviceFullName}
    roleFullName: zookeeper-server
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: zookeeper-server
  ports:
  - name: metrics
    port: ${conf['zookeeper.metrics.port']}