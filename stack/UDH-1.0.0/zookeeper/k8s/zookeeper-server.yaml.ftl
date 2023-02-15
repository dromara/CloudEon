---
apiVersion: "apps/v1"
kind: "Deployment"
metadata:
  labels:
    name: "zookeeper-server-${service.serviceName}"
  name: "zookeeper-server-${service.serviceName}"
  namespace: "default"
spec:
  replicas: 3
  selector:
    matchLabels:
      app: "zookeeper-server-${service.serviceName}"
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
        name: "zookeeper-server-${service.serviceName}"
        app: "zookeeper-server-${service.serviceName}"
        podConflictName: "zookeeper-server-${service.serviceName}"
      annotations:
        serviceInstanceName: "${service.serviceName}"
    spec:
      affinity:
        podAntiAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
          - labelSelector:
              matchLabels:
                name: "zookeeper-server-${service.serviceName}"
                podConflictName: "zookeeper-server-${service.serviceName}"
            namespaces:
            - "default"
            topologyKey: "kubernetes.io/hostname"
      hostPID: false
      hostNetwork: true
      containers:
      - args:
        - "boot.sh"
        - "ZOOKEEPER"
        env:
        - name: "ZOOCFGDIR"
          value: "/opt/udh/${service.serviceName}/conf"
        image: "${dockerImage}"
        imagePullPolicy: "Always"
        name: "zookeeper-server-${service.serviceName}"
        resources:
          requests: {}
          limits: {}
        securityContext:
          privileged: true
        volumeMounts:
        - mountPath: "/opt/udh/${service.serviceName}/data"
          name: "data"
        - mountPath: "/opt/udh/${service.serviceName}/log"
          name: "log"
        - mountPath: "/etc/localtime"
          name: "timezone"
        - mountPath: "/opt/udh/${service.serviceName}/conf"
          name: "conf"

      nodeSelector:
        zookeeper-server-${service.serviceName}: "true"
      terminationGracePeriodSeconds: 30
      volumes:
      - hostPath:
          path: "/opt/udh/${service.serviceName}/data"
        name: "data"
      - hostPath:
          path: "/opt/udh/${service.serviceName}/log"
        name: "log"
      - hostPath:
          path: "/etc/localtime"
        name: "timezone"
      - hostPath:
          path: "/opt/udh/${service.serviceName}/conf"
        name: "conf"

