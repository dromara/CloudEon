---
apiVersion: "apps/v1"
kind: "Deployment"
metadata:
  labels:
    name: "${roleServiceFullName}"
  name: "${roleServiceFullName}"
  namespace: ${namespace}
spec:
  replicas: ${roleNodeCnt}
  selector:
    matchLabels:
      app: "${roleServiceFullName}"
  strategy:
    type: "RollingUpdate"
    rollingUpdate:
      maxSurge: 0
      maxUnavailable: 1
  minReadySeconds: 5
  revisionHistoryLimit: 10
  template:
    metadata:
      labels:
        name: "${roleServiceFullName}"
        app: "${roleServiceFullName}"
        podConflictName: "${roleServiceFullName}"
      annotations:
        serviceInstanceName: "${service.serviceName}"
    spec:
      affinity:
        podAntiAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
          - labelSelector:
              matchLabels:
                name: "${roleServiceFullName}"
                podConflictName: "${roleServiceFullName}"
            namespaces:
            - "${namespace}"
            topologyKey: "kubernetes.io/hostname"
      hostPID: false
      hostNetwork: true
      containers:
      - args:
          - "/opt/edp/${service.serviceName}/conf/bootstrap-filebeat.sh"
        env:
          - name: "USER"
            value: "kyuubi"
          - name: "KYUUBI_CONF_DIR"
            value: "/opt/edp/${service.serviceName}/conf"
        image: "${dockerImage}"
        imagePullPolicy: "Always"
        readinessProbe:
          exec:
            command:
            - "/bin/bash"
            - "-c"
            - "curl --fail --connect-timeout 15 --max-time 15 \"http://`hostname`:${conf['http.port']}/\"\
            \n"
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        name: "${roleServiceFullName}"
        resources:
          requests: {}
          limits: {}
        securityContext:
          privileged: true
        volumeMounts:
        - mountPath: "/opt/edp/${service.serviceName}/data"
          name: "data"
        - mountPath: "/opt/edp/${service.serviceName}/log"
          name: "log"
        - mountPath: "/etc/localtime"
          name: "timezone"
        - mountPath: "/opt/edp/${service.serviceName}/conf"
          name: "conf"
        - mountPath: "/opt/edp/yarn/log"
          name: "yarn-log"
        - mountPath: "/opt/edp/hdfs/log"
          name: "hdfs-log"
        - mountPath: "/opt/edp/zookeeper/log"
          name: "zk-log"
        - mountPath: "/opt/edp/hbase/log"
          name: "hbase-log"
        - mountPath: "/opt/edp/hive/log"
          name: "hive-log"
        - mountPath: "/opt/edp/kafka/log"
          name: "kafka-log"
        - mountPath: "/opt/edp/flink/log"
          name: "flink-log"
        - mountPath: "/opt/edp/spark/log"
          name: "spark-log"
        - mountPath: "/opt/edp/trino/log"
          name: "trino-log"
        - mountPath: "/opt/edp/kyuubi/log"
          name: "kyuubi-log"
        - mountPath: "/opt/edp/kylin/log"
          name: "kylin-log"
        - mountPath: "/opt/edp/doris/log"
          name: "doris-log"
        - mountPath: "/opt/edp/elasticsearch/log"
          name: "elasticsearch-log"
        - mountPath: "/opt/edp/amoro/log"
          name: "amoro-log"

      nodeSelector:
        ${roleServiceFullName}: "true"
      terminationGracePeriodSeconds: 30
      volumes:
      - hostPath:
          path: "/opt/edp/${service.serviceName}/data"
        name: "data"
      - hostPath:
          path: "/opt/edp/${service.serviceName}/log"
        name: "log"
      - hostPath:
          path: "/etc/localtime"
        name: "timezone"
      - hostPath:
          path: "/opt/edp/${service.serviceName}/conf"
        name: "conf"
      - hostPath:
          path: "/opt/edp/yarn/log"
        name: "yarn-log"
      - hostPath:
          path: "/opt/edp/hdfs/log"
        name: "hdfs-log"
      - hostPath:
          path: "/opt/edp/zookeeper/log"
        name: "zk-log"
      - hostPath:
          path: "/opt/edp/hbase/log"
        name: "hbase-log"
      - hostPath:
          path: "/opt/edp/hive/log"
        name: "hive-log"
      - hostPath:
          path: "/opt/edp/kafka/log"
        name: "kafka-log"
      - hostPath:
          path: "/opt/edp/flink/log"
        name: "flink-log"
      - hostPath:
          path: "/opt/edp/spark/log"
        name: "spark-log"
      - hostPath:
          path: "/opt/edp/trino/log"
        name: "trino-log"
      - hostPath:
          path: "/opt/edp/kyuubi/log"
        name: "kyuubi-log"
      - hostPath:
          path: "/opt/edp/kylin/log"
        name: "kylin-log"
      - hostPath:
          path: "/opt/edp/doris/log"
        name: "doris-log"
      - hostPath:
          path: "/opt/edp/elasticsearch/log"
        name: "elasticsearch-log"
      - hostPath:
          path: "/opt/edp/amoro/log"
        name: "amoro-log"
