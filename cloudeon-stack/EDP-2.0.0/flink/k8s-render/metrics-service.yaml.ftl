kind: Service
apiVersion: v1
metadata:
  name: metrics-flink-historyserver
  labels:
    sname: ${serviceFullName}
    roleFullName: flink-historyserver
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: flink-historyserver
  ports:
  - name: metrics
    port: 5555
