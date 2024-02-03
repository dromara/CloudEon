kind: Service
apiVersion: v1
metadata:
  name: metrics-hadoop-hdfs-journalnode
  labels:
    sname: ${serviceFullName}
    roleFullName: hadoop-hdfs-journalnode
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hadoop-hdfs-journalnode
  ports:
  - name: metrics
    port: 5546
---
kind: Service
apiVersion: v1
metadata:
  name: metrics-hadoop-hdfs-namenode
  labels:
    sname: ${serviceFullName}
    roleFullName: hadoop-hdfs-namenode
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hadoop-hdfs-namenode
  ports:
  - name: metrics
    port: 5542
---
kind: Service
apiVersion: v1
metadata:
  name: metrics-hadoop-hdfs-zkfc
  labels:
    sname: ${serviceFullName}
    roleFullName: hadoop-hdfs-namenode
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hadoop-hdfs-namenode
  ports:
  - name: metrics
    port: 5544
---
kind: Service
apiVersion: v1
metadata:
  name: metrics-hadoop-hdfs-datanode
  labels:
    sname: ${serviceFullName}
    roleFullName: hadoop-hdfs-datanode
    enable-default-service-monitor: true
spec:
  selector:
    sname: ${serviceFullName}
    roleFullName: hadoop-hdfs-datanode
  ports:
  - name: metrics
    port: 5545
---