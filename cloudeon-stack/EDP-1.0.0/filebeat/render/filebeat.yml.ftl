filebeat.inputs:


  ########################################## yarn ##########################################

  - type: log
    enabled: true
    fields:
      service: YARN
      role: YARN_RESOURCEMANAGER
    paths:
      - /opt/edp/yarn/log/hadoop-hadoop-resourcemanager-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m


  - type: log
    enabled: true
    fields:
      service: YARN
      role: YARN_NODEMANAGER
    paths:
      - /opt/edp/yarn/log/hadoop-hadoop-nodemanager-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m



  ########################################## hdfs ##########################################


  ########################################## output ##########################################
<#if dependencies.ELASTICSEARCH??>
  <#assign es=dependencies.ELASTICSEARCH esNodes=[]>
  <#list es.serviceRoles['ELASTICSEARCH_NODE'] as role>
    <#assign esNodes += ['"'+role.hostname + ":" + es.conf["elasticsearch.http.listeners.port"]+'"']>
  </#list>
output:
  elasticsearch:
    hosts: [${esNodes?join(",")}]
</#if>



processors:
  - script:
      lang: javascript
      source: >
        function process(event) {
          var match = event.Get("message").match(/(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})/);
          if (match) {
            event.Put("biz_time", match[0]);
          }
          var match2 = event.Get("message").match(/(\b(WARN|ERROR)\b)/);
          if (match2) {
            event.Put("log_level", match2[0]);
          }
          return event;
        }
  # 将biz_time作为@timestamp
  - timestamp:
      field: biz_time
      timezone: Asia/Shanghai
      layouts:
        - '2006-01-02 15:04:05'
      test:
        - '2019-06-22 16:33:51'

  - add_fields:
      target: ''
      fields:
        hostip: "${localhostip}"
        hostname: "${localhostname}"

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



filebeat.registry.flush: ${conf['registry.flush']}
logging.level: info
logging.files:
  path: /opt/edp/${service.serviceName}/log
  name: filebeat.log

path.data: /opt/edp/${service.serviceName}/data