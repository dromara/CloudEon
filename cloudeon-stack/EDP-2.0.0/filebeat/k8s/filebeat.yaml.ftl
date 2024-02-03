apiVersion: apps.kruise.io/v1alpha1
kind: SidecarSet
metadata:
  name: filebeat-sidecarset
spec:
  selector:
    # 需要注入 sidecar 容器的 pod labels
    matchLabels:
      inject-filebeat: "true"
  # sidecarSet默认是对整个集群生效，可以通过namespace字段指定生效的范围
  namespace: ${namespace}
  containers:
  - name: filebeat
    image: "${conf['serverImage']}"
    imagePullPolicy: "${conf['global.imagePullPolicy']}"
#  注入到pod原containers的前面
    podInjectPolicy: AfterAppContainer
# 配置为 enabled 后会把应用容器中所有挂载点注入 sidecar 同一路经下(sidecar中本身就有声明的数据卷和挂载点除外）
    shareVolumePolicy:
      type: enabled
# 设置root用户运行，避免写入filebeat.registry失败
    securityContext:
      runAsUser: 0
    env:
      - name: NODE_NAME
        valueFrom:
          fieldRef:
            fieldPath: spec.nodeName
      - name: POD_NAME
        valueFrom:
          fieldRef:
            fieldPath: metadata.name
      - name: POD_IP
        valueFrom:
          fieldRef:
            fieldPath: status.podIP
<#--禁用filebeat 检测，避免因filebeat状态异常影响安装-->
<#--    readinessProbe:-->
<#--      exec:-->
<#--        command:-->
<#--        - sh-->
<#--        - -c-->
<#--        - |-->
<#--          #!/usr/bin/env bash -e-->
<#--          filebeat test output-->
    resources:
      limits:
        cpu: "1"
        memory: 200Mi
      requests:
        cpu: 10m
        memory: 100Mi
    volumeMounts:
    - mountPath: /usr/share/filebeat/filebeat.yml
      name: filebeat-config
      subPath: filebeat.yml
  volumes:
  - configMap:
      name: filebeat-common-config
    name: filebeat-config

