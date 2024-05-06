ams:
  admin-username: admin
  admin-password: ${conf['ams.admin.password']}
  server-bind-host: "0.0.0.0"
  server-expose-host: "${nodeInfo[HOSTNAME]["ip"]}"

  thrift-server:
    max-message-size: 104857600 # 100MB
    selector-thread-count: 2
    selector-queue-size: 4
    table-service:
      bind-port: ${conf['table-service.port']}
      worker-thread-count: 20
    optimizing-service:
      bind-port: ${conf['optimizing-service.port']}

  http-server:
    bind-port: ${conf['http-server.port']}

  refresh-external-catalogs:
    interval: 180000 # 3min
    thread-count: 10
    queue-size: 1000000

  refresh-tables:
    thread-count: 10
    interval: 60000 # 1min
    
  self-optimizing:
    commit-thread-count: 10

  optimizer:
    heart-beat-timeout: 60000 # 1min
    task-ack-timeout: 30000 # 30s

  blocker:
    timeout: 60000 # 1min

  # optional features
  expire-snapshots:
    enabled: true
    thread-count: 10

  clean-orphan-files:
    enabled: true
    thread-count: 10

  sync-hive-tables:
    enabled: true
    thread-count: 10

  data-expiration:
    enabled: false
    thread-count: 10
    interval: 1d

  database:
<#--    type: derby-->
<#--    jdbc-driver-class: org.apache.derby.jdbc.EmbeddedDriver-->
<#--    url: jdbc:derby:/tmp/amoro/derby;create=true-->

  #  MySQL database configuration.
  database:
    type: mysql
    jdbc-driver-class: com.mysql.cj.jdbc.Driver
    url: ${conf['ams.mysql.ConnectionURL']}
    username: ${conf['ams.mysql.ConnectionUserName']}
    password: ${conf['ams.mysql.ConnectionPassword']}

  terminal:
    backend: local
    local.spark.sql.iceberg.handle-timestamp-without-timezone: false

#  Kyuubi terminal backend configuration.
#  terminal:
#    backend: kyuubi
#    kyuubi.jdbc.url: jdbc:hive2://127.0.0.1:10009/


#  High availability configuration.
#  ha:
#    enabled: true
#    cluster-name: default
#    zookeeper-address: 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183

containers:
  - name: localContainer
    container-impl: com.netease.arctic.server.manager.LocalOptimizerContainer
    properties:
      export.JAVA_HOME: "/opt/jdk1.8.0"   # JDK environment


  - name: flinkContainer
    container-impl: com.netease.arctic.server.manager.FlinkOptimizerContainer
    properties:
      flink-home: /opt/flink/                                     # Flink install home
      target: yarn-per-job
#      export.JVM_ARGS: "-Djava.security.krb5.conf=/opt/krb5.conf"   # Flink launch jvm args, like kerberos config when ues kerberos
      export.HADOOP_CONF_DIR: "/opt/hadoop/etc/hadoop/"                   # Hadoop config dir
      export.HADOOP_USER_NAME: "root"                             # Hadoop user submit on yarn
      export.FLINK_CONF_DIR: "/opt/flink/conf/"                    # Flink config dir
      export.HADOOP_CLASSPATH: "$(hadoop classpath)"
