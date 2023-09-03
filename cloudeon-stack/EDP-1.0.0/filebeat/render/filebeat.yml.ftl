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

  - type: log
    enabled: true
    fields:
      service: YARN
      role: YARN_HISTORYSERVER
    paths:
      - /opt/edp/yarn/log/hadoop-yarn-historyserver-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  - type: log
    enabled: true
    fields:
      service: YARN
      role: YARN_TIMELINESERVER
    paths:
      - /opt/edp/yarn/log/hadoop-hadoop-timelineserver-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## hdfs ##########################################
  - type: log
    enabled: true
    fields:
      service: HDFS
      role: HDFS_JOURNALNODE
    paths:
      - /opt/edp/hdfs/log/hadoop-hadoop-journalnode-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m


  - type: log
    enabled: true
    fields:
      service: HDFS
      role: HDFS_NAMENODE
    paths:
      - /opt/edp/hdfs/log/hadoop-hadoop-namenode-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  - type: log
    enabled: true
    fields:
      service: HDFS
      role: HDFS_DATANODE
    paths:
      - /opt/edp/hdfs/log/hadoop-hadoop-datanode-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  - type: log
    enabled: true
    fields:
      service: HDFS
      role: HDFS_HTTPFS
    paths:
      - /opt/edp/hdfs/log/httpfs.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## zookeeper ##########################################

  - type: log
    enabled: true
    fields:
      service: ZOOKEEPER
      role: ZOOKEEPER_SERVER
    paths:
      - /opt/edp/zookeeper/log/zookeeper-zookeeper-server-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## hbase ##########################################

  - type: log
    enabled: true
    fields:
      service: HBASE
      role: HBASE_MASTER
    paths:
      - /opt/edp/hbase/log/hbase-hbase-master-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  - type: log
    enabled: true
    fields:
      service: HBASE
      role: HBASE_REGIONSERVER
    paths:
      - /opt/edp/hbase/log/hbase-hbase-regionserver-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m


  ########################################## hive ##########################################

  - type: log
    enabled: true
    fields:
      service: HIVE
      role: HIVE_METASTORE
    paths:
      - /opt/edp/hive/log/hive-metastore-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  - type: log
    enabled: true
    fields:
      service: HIVE
      role: HIVE_SERVER2
    paths:
      - /opt/edp/hive/log/hive-server2-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## kafka ##########################################

  - type: log
    enabled: true
    fields:
      service: KAFKA
      role: KAFKA_BROKER
    paths:
      - /opt/edp/kafka/log/server.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m


  ########################################## flink ##########################################

  - type: log
    enabled: true
    fields:
      service: FLINK
      role: FLINK_HISTORY_SERVER
    paths:
      - /opt/edp/flink/log/flink-flink-historyserver-0-*.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## spark ##########################################

  - type: log
    enabled: true
    fields:
      service: SPARK
      role: SPAKR_THRIFT_SERVER
    paths:
      - /opt/edp/spark/log/spark-spark-org.apache.spark.sql.hive.thriftserver.HiveThriftServer2-1-*.out
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  - type: log
    enabled: true
    fields:
      service: SPARK
      role: SPARK_HISTORY_SERVER
    paths:
      - /opt/edp/spark/log/spark-spark-org.apache.spark.deploy.history.HistoryServer-1-*.out
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## trino ##########################################

  - type: log
    enabled: true
    fields:
      service: TRINO
      role: TRINO_COORDINATOR
    paths:
      - /opt/edp/trino/log/coordinator-server.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  - type: log
    enabled: true
    fields:
      service: TRINO
      role: TRINO_WORKER
    paths:
      - /opt/edp/trino/log/worker-server.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## kyuubi ##########################################

  - type: log
    enabled: true
    fields:
      service: KYUUBI
      role: KYUUBI_SERVER
    paths:
      - /opt/edp/kyuubi/log/kyuubi-kyuubi-org.apache.kyuubi.server.KyuubiServer-*.out
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## kylin ##########################################

  - type: log
    enabled: true
    fields:
      service: KYLIN
      role: KYLIN_SERVER
    paths:
      - /opt/edp/kylin/log/kylin.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## doris ##########################################

  - type: log
    enabled: true
    fields:
      service: DORIS
      role: DORIS_FE
    paths:
      - /opt/edp/doris/log/fe.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  - type: log
    enabled: true
    fields:
      service: DORIS
      role: DORIS_BE
    paths:
      - /opt/edp/doris/log/be.INFO
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## elasticsearch ##########################################

  - type: log
    enabled: true
    fields:
      service: ELASTICSEARCH
      role: ELASTICSEARCH_NODE
    paths:
      - /opt/edp/elasticsearch/log/elasticsearch.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

  ########################################## amoro ##########################################

  - type: log
    enabled: true
    fields:
      service: AMORO
      role: AMORO_AMS
    paths:
      - /opt/edp/amoro/log/ams-info.log
    multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
    multiline.negate: true
    multiline.match: after
    close_timeout: 5m

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
http.enabled: true
http.port: ${conf['http.port']}
http.host: ${localhostname}