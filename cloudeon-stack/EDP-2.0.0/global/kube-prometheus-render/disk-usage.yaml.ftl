apiVersion: monitoring.coreos.com/v1
kind: PrometheusRule
metadata:
  creationTimestamp: null
  labels:
    role: alert-rules
  name: node-disk-usage
spec:
  groups:
    - name: node-disk-usage
      rules:
      - alert: node-disk-usage
        expr: 100*(1-node_filesystem_avail_bytes{mountpoint="/"}/node_filesystem_size_bytes{mountpoint="/"} ) > ${conf["alert.node.disk.thresholdValue"]}
        for: 1m
        labels:
          groupId: node-disk-usage
          userIds: super
          receivers: MY_REC
        annotations:
          title: "磁盘警告：节点{{$labels.instance}}的 {{$labels.mountpoint}} 目录使用率超过阈值"
          content: "磁盘警告：节点{{$labels.instance}}的 {{$labels.mountpoint}} 目录使用率已达到{{$value}}%"

