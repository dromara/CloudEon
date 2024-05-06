kind: Service
apiVersion: v1
metadata:
  name: metrics-hbase-master
  labels:
    sname: ${serviceFullName}
    roleFullName: hbase-master
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hbase-master
  ports:
  - name: metrics
    port: 5552
---
kind: Service
apiVersion: v1
metadata:
  name: metrics-hbase-regionserver
  labels:
    sname: ${serviceFullName}
    roleFullName: hbase-regionserver
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hbase-regionserver
  ports:
  - name: metrics
    port: 5553