kind: Service
apiVersion: v1
metadata:
  name: metrics-kafka
  labels:
    sname: ${serviceFullName}
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
  ports:
  - name: metrics
    port: 5551