kind: Service
apiVersion: v1
metadata:
  name: metrics-hadoop-yarn-resourcemanager
  labels:
    sname: ${serviceFullName}
    roleFullName: hadoop-yarn-resourcemanager
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hadoop-yarn-resourcemanager
  ports:
  - name: metrics
    port: 5548
---
kind: Service
apiVersion: v1
metadata:
  name: metrics-hadoop-yarn-nodemanager
  labels:
    sname: ${serviceFullName}
    roleFullName: hadoop-yarn-nodemanager
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hadoop-yarn-nodemanager
  ports:
  - name: metrics
    port: 5547