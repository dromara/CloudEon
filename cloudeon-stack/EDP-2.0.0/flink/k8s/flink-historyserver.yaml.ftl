---
apiVersion: "apps/v1"
kind: "Deployment"
metadata:
  labels:
    name: "${roleServiceFullName}"
    sname: "${serviceFullName}"
    roleFullName: "${roleFullName}"
  name: "${roleServiceFullName}"
spec:
  replicas: ${roleNodeCnt}
  selector:
    matchLabels:
      app: "${roleServiceFullName}"
      sname: "${serviceFullName}"
      roleFullName: "${roleFullName}"
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
        sname: "${serviceFullName}"
        roleFullName: "${roleFullName}"
        app: "${roleServiceFullName}"
        podConflictName: "${roleServiceFullName}"
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
        name: "${roleServiceFullName}"
        command: ["/bin/bash","-c"]
        args:
          - |
            /bin/bash /opt/global/bootstrap.sh && \
            /bin/bash /opt/service-common/bootstrap.sh;
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
        resources:
          requests:
            memory: "${conf['flink.hs.container.request.memory']}Mi"
            cpu: "${conf['flink.hs.container.request.cpu']}"
          limits:
            memory: "${conf['flink.hs.container.limit.memory']}Mi"
            cpu: "${conf['flink.hs.container.limit.cpu']}"
        env:
          - name: NODE_NAME
            valueFrom:
              fieldRef:
                fieldPath: spec.nodeName
          - name: MEM_LIMIT
            valueFrom:
              resourceFieldRef:
                resource: limits.memory
          - name: RENDER_TPL_DIR
            value: "/opt/service-render"
          - name: RENDER_MODEL
            value: "/opt/service-common/values.json"
          - name: ROLE_FULL_NAME
            value: "${roleFullName}"
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
        - name: hdfs-config
          mountPath: /etc/hdfs-config
        - name: yarn-config
          mountPath: /etc/yarn-config
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
          name: flink-service-render
      - name: service-common
        configMap:
          name: flink-service-common
      - hostPath:
          path: "/opt/edp/${roleFullName}"
          type: DirectoryOrCreate
        name: "workspace"
      - name: hdfs-config
        configMap:
          name: hdfs-config
      - name: yarn-config
        configMap:
          name: yarn-config