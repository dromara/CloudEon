apiVersion: monitoring.coreos.com/v1
kind: PrometheusRule
metadata:
  creationTimestamp: null
  labels:
    role: alert-rules
  name: node-memory-usage
spec:
  groups:
    - name: node-memory-usage
      rules:
      - alert: node-memory-usage
        expr: 100 -( node_memory_MemFree_bytes{job="node-exporter"}/ node_memory_MemTotal_bytes{job="node-exporter"}* 100)  > ${conf["alert.node.memory.thresholdValue"]}
        for: 1m
        labels:
          groupId: node-memory-usage
          userIds: super
          receivers: MY_REC
        annotations:
          title: "磁盘警告：节点{{$labels.instance}}的 内存使用率超过阈值"
          content: "磁盘警告：节点{{$labels.instance}}的 内存使用率已达到{{$value}}%"

