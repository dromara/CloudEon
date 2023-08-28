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
          - "/opt/edp/${service.serviceName}/conf/bootstrap-kafkaserver.sh"

        image: "${dockerImage}"
        imagePullPolicy: "Always"
        readinessProbe:
          tcpSocket:
            port: ${conf['kafka.listeners.port']}
          initialDelaySeconds: 10
          timeoutSeconds: 2
        name: "${roleServiceFullName}"
        resources:
          requests:
            memory: "${conf['kafka.container.request.memory']}Mi"
            cpu: "${conf['kafka.container.request.cpu']}"
          limits:
            memory: "${conf['kafka.container.limit.memory']}Mi"
            cpu: "${conf['kafka.container.limit.cpu']}"
        env:
          - name: MEM_LIMIT
            valueFrom:
              resourceFieldRef:
                resource: limits.memory
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

