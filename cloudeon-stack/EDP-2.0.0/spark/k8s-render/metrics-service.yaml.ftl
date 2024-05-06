kind: Service
apiVersion: v1
metadata:
  name: metrics-spark-historyserver
  labels:
    sname: ${serviceFullName}
    roleFullName: spark-historyserver
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: spark-historyserver
  ports:
  - name: metrics
    port: 5554
