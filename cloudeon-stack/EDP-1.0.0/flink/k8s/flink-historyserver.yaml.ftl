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
          - "/opt/edp/${service.serviceName}/conf/bootstrap-historyserver.sh"
        env:
          - name: "FLINK_CONF_DIR"
            value: "/opt/edp/${service.serviceName}/conf"
          - name: HADOOP_CLASSPATH
            value: "/home/flink/apache-hadoop/share/hadoop/common/lib/*:/home/flink/apache-hadoop/share/hadoop/common/*:/home/flink/apache-hadoop/share/hadoop/hdfs:/home/flink/apache-hadoop/share/hadoop/hdfs/lib/*:/home/flink/apache-hadoop/share/hadoop/hdfs/*:/home/flink/apache-hadoop/share/hadoop/mapreduce/*:/home/flink/apache-hadoop/share/hadoop/yarn:/home/flink/apache-hadoop/share/hadoop/yarn/lib/*:/home/flink/apache-hadoop/share/hadoop/yarn/*"
          - name: "HADOOP_CONF_DIR"
            value: "/opt/edp/${service.serviceName}/conf"
          - name: "USER"
            value: "flink"
          - name: MEM_LIMIT
            valueFrom:
              resourceFieldRef:
                resource: limits.memory
        image: "${dockerImage}"
        imagePullPolicy: "Always"
        readinessProbe:
          exec:
            command:
            - "/bin/bash"
            - "-c"
            - "curl --fail --connect-timeout 15 --max-time 15 \"http://`hostname`:${conf['flink.history.ui.port']}/\"\
            \n"
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        name: "${roleServiceFullName}"
        resources:
          requests:
            memory: "${conf['flink.hs.container.request.memory']}Mi"
            cpu: "${conf['flink.hs.container.request.cpu']}"
          limits:
            memory: "${conf['flink.hs.container.limit.memory']}Mi"
            cpu: "${conf['flink.hs.container.limit.cpu']}"
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

