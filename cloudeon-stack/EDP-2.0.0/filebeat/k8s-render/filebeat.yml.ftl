kind: ConfigMap
metadata:
  name: filebeat-common-config
  labels:
    sname: ${serviceFullName}
apiVersion: v1
data:
  filebeat.yml: |
    filebeat.config.inputs:
      enabled: true
      path: /workspace/filebeat/inputs/*.yml
      reload.enabled: true
      reload.period: 10s
    filebeat.config.modules:
      enabled: true
      path: /workspace/filebeat/modules/*.yml
      reload.enabled: true
      reload.period: 10s
    filebeat.registry.path: /workspace/filebeat/registry
    http.enabled: false
    output.elasticsearch:
      hosts: [${dependencies.ELASTICSEARCH.serviceRoles['ELASTICSEARCH_NODE']?map(role -> '"' + role.hostname+":"+dependencies.ELASTICSEARCH.conf["elasticsearch.http.listeners.port"]+'"')?join(',')}]
<#noparse >
    processors:
      - script:
          lang: javascript
          source: >
            function process(event) {
              var match = event.Get("message").match(/(\d{4}-\d{2}-\d{2}[ T]\d{2}:\d{2}:\d{2})/);
              if (match) {
                event.Put("bizTime", match[0]);
              }
              var match2 = event.Get("message").match(/(\b(WARN|ERROR|DEBUG|INFO|TRACE)\b)/);
              if (match2) {
                event.Put("logLevel", match2[0]);
              }
              return event;
            }
      # 将bizTime作为@timestamp
      - timestamp:
          field: bizTime
          timezone: Asia/Shanghai
          layouts:
            - '2006-01-02 15:04:05'
          test:
            - '2019-06-22 16:33:51'
      - add_fields:
          target: ''
          fields:
            podname: "${POD_NAME}"
            nodename: "${NODE_NAME}"
      - rename:
          fields:
            - from: "log.offset"
              to: "offset"
            - from: "log.file.path"
              to: "filePath"
            - from: "fields.service"
              to: "serviceName"
            - from: "fields.role"
              to: "roleName"
      - drop_fields:
          fields: ["agent", "ecs", "input", "host","log","fields"]
      - drop_event:
          when.or:
            - contains:
                message: "TRACE"
            - contains:
                message: "DEBUG"
            - contains:
                message: "DEBU"
</#noparse>