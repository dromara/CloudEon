kind: Service
apiVersion: v1
metadata:
  name: metrics-hive-metastore
  labels:
    sname: ${serviceFullName}
    roleFullName: hive-metastore
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hive-metastore
  ports:
  - name: metrics
    port: 5549
---
kind: Service
apiVersion: v1
metadata:
  name: metrics-hive-server2
  labels:
    sname: ${serviceFullName}
    roleFullName: hive-server2
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hive-server2
  ports:
  - name: metrics
    port: 5550