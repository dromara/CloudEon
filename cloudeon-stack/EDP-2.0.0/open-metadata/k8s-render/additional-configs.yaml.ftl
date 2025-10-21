apiVersion: v1
kind: Service
metadata:
  name: openmetadata-web
  namespace: ${conf["open-metadata.namespace"]}
spec:
  type: NodePort
  <#-- 这些选择器可在 openmetadata 部署后通过 `kubectl -n open-metadata describe deployments.apps openmetadata` 查看到 -->
  selector:
    app.kubernetes.io/instance: open-metadata-open-metadata
    app.kubernetes.io/name: openmetadata
  ports:
  - name: service-port
    port: ${conf["open-metadata.service.port"]}
    targetPort: ${conf["open-metadata.service.port"]}
    nodePort: ${conf["open-metadata.service.node-port"]}
    protocol: TCP
  - name: metrics-port
    port: ${conf["open-metadata.service.metrics-port"]}
    targetPort: ${conf["open-metadata.service.metrics-port"]}
    nodePort: ${conf["open-metadata.service.node-metrics-port"]}
    protocol: TCP
