---
apiVersion: "apps/v1"
kind: "Deployment"
metadata:
  labels:
    name: "${roleServiceFullName}"
    sname: ${serviceFullName}
  name: "${roleServiceFullName}"
spec:
  replicas: ${roleNodeCnt}
  selector:
    matchLabels:
      app: "${roleServiceFullName}"
      sname: ${serviceFullName}
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
        sname: ${serviceFullName}
    spec:
      affinity:
        podAntiAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
          - labelSelector:
              matchLabels:
                name: "${roleServiceFullName}"
                podConflictName: "${roleServiceFullName}"
            topologyKey: "kubernetes.io/hostname"
      hostPID: false
      hostNetwork: true
      containers:
      - image: "${conf['serverImage']}"
        imagePullPolicy: "${conf['global.imagePullPolicy']}"
        command: ["/bin/bash","-c"]
        args:
          - |
            /bin/bash /opt/global/bootstrap.sh && \
            /bin/bash /opt/service-common/bootstrap.sh;
        readinessProbe:
          exec:
            command:
            - "/bin/bash"
            - "/opt/service-common/readiness.sh"
          failureThreshold: 3
          initialDelaySeconds: 3
          periodSeconds: 30
          successThreshold: 1
          timeoutSeconds: 15
        name: "${roleServiceFullName}"
        resources:
          requests:
            memory: "${conf['zookeeper.container.request.memory']}Mi"
            cpu: "${conf['zookeeper.container.request.cpu']}"
          limits:
            memory: "${conf['zookeeper.container.limit.memory']}Mi"
            cpu: "${conf['zookeeper.container.limit.cpu']}"
        env:
        - name: NODE_NAME
          valueFrom:
            fieldRef:
              fieldPath: spec.nodeName
        - name: MEM_LIMIT
          valueFrom:
            resourceFieldRef:
              resource: limits.memory
        - name: ZK_CLIENT_PORT
          value: "${conf['zookeeper.client.port']}"
        - name: RENDER_TPL_DIR
          value: "/opt/service-render"
        - name: RENDER_MODEL
          value: "/opt/service-common/values.json"
        securityContext:
          privileged: true
        volumeMounts:
        - mountPath: "/etc/localtime"
          name: "timezone"
        - name: global-service-common
          mountPath: /opt/global/bootstrap.sh
          subPath: bootstrap.sh
        - name: global-render-config
          mountPath: /opt/global/10.render
        - name: global-usersync-config
          mountPath: /opt/global/20.usersync
        - name: service-render
          mountPath: /opt/service-render
        - name: service-common
          mountPath: /opt/service-common
        - mountPath: "/workspace"
          name: "workspace"
      nodeSelector:
        ${roleServiceFullName}: "true"
      terminationGracePeriodSeconds: 30
      volumes:
      - hostPath:
          path: "/etc/localtime"
        name: "timezone"
      - name: global-service-common
        configMap:
          name: global-service-common
      - name: global-render-config
        configMap:
          name: global-render-config
      - name: global-usersync-config
        configMap:
          name: global-usersync-config
      - name: service-render
        configMap:
          name: zookeeper-service-render
      - name: service-common
        configMap:
          name: zookeeper-service-common
      - hostPath:
          path: "/opt/edp/${roleFullName}"
          type: DirectoryOrCreate
        name: "workspace"

