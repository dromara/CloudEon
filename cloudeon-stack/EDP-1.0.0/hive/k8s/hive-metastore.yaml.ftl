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
          - "/opt/edp/${service.serviceName}/conf/bootstrap-metastore.sh"
        env:
          - name: "SERVICE_NAME"
            value: "metastore"
          - name: HADOOP_CONF_DIR
            value: /opt/edp/${service.serviceName}/conf
          - name: "HIVE_CONF_DIR"
            value: "/opt/edp/${service.serviceName}/conf"
          - name: MEM_LIMIT
            valueFrom:
              resourceFieldRef:
                resource: limits.memory
        image: "${dockerImage}"
        imagePullPolicy: "Always"
        readinessProbe:
          tcpSocket:
            port: ${conf['hive.metastore.thrift.port']}
          initialDelaySeconds: 10
          timeoutSeconds: 2
        name: "${roleServiceFullName}"
        resources:
          requests:
            memory: "${conf['hive.metastore.container.request.memory']}Mi"
            cpu: "${conf['hive.metastore.container.request.cpu']}"
          limits:
            memory: "${conf['hive.metastore.container.limit.memory']}Mi"
            cpu: "${conf['hive.metastore.container.limit.cpu']}"
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

