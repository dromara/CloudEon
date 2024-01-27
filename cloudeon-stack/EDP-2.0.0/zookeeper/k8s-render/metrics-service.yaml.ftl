kind: Service
apiVersion: v1
metadata:
  name: metrics-zookeeper
  labels:
    sname: ${serviceFullName}
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
  ports:
  - name: metrics
    port: ${conf['zookeeper.metrics.port']}