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
<#switch roleFullName>
  <#case "hadoop-yarn-resourcemanager">
        readinessProbe:
          exec:
            command:
            - "/bin/bash"
            - "-c"
            - "curl --fail --connect-timeout 15 --max-time 15 \"http://`hostname`:${conf['resourcemanager.webapp.port']}/?user.name=yarn\"\
            \n"
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        resources:
          requests:
            memory: "${conf['hadop.yarn.rm.container.request.memory']}Mi"
            cpu: "${conf['hadop.yarn.rm.container.request.cpu']}"
          limits:
            memory: "${conf['hadop.yarn.rm.container.limit.memory']}Mi"
            cpu: "${conf['hadop.yarn.rm.container.limit.cpu']}"
      <#break>
  <#case "hadoop-yarn-nodemanager">
        readinessProbe:
          exec:
            command:
            - "/bin/bash"
            - "-c"
            - "curl --fail --connect-timeout 15 --max-time 15 \"http://`hostname`:${conf['nodemanager.webapp.port']}/?user.name=yarn\"\
              \n"
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        resources:
          requests:
            memory: "${conf['hadop.yarn.nm.container.request.memory']}Mi"
            cpu: "${conf['hadop.yarn.nm.container.request.cpu']}"
          limits:
            memory: "${conf['hadop.yarn.nm.container.limit.memory']}Mi"
            cpu: "${conf['hadop.yarn.nm.container.limit.cpu']}"
    <#break>
  <#case "hadoop-yarn-historyserver">
        readinessProbe:
          exec:
            command:
              - "/bin/bash"
              - "-c"
              - "curl --fail --connect-timeout 15 --max-time 15 \"http://`hostname`:${conf['historyserver.http-port']}/?user.name=yarn\"\
              \n"
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        resources:
          requests:
            memory: "${conf['hadop.yarn.hs.container.request.memory']}Mi"
            cpu: "${conf['hadop.yarn.hs.container.request.cpu']}"
          limits:
            memory: "${conf['hadop.yarn.hs.container.limit.memory']}Mi"
            cpu: "${conf['hadop.yarn.hs.container.limit.cpu']}"
    <#break>
  <#case "hadoop-yarn-timelineserver">
        readinessProbe:
          tcpSocket:
            port: ${conf['timelineserver.http.port']}
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        resources:
          requests:
            memory: "${conf['hadop.yarn.tl.container.request.memory']}Mi"
            cpu: "${conf['hadop.yarn.tl.container.request.cpu']}"
          limits:
            memory: "${conf['hadop.yarn.tl.container.limit.memory']}Mi"
            cpu: "${conf['hadop.yarn.tl.container.limit.cpu']}"
    <#break>
</#switch>
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
          name: yarn-service-render
      - name: service-common
        configMap:
          name: yarn-service-common
      - hostPath:
          path: "/opt/edp/${roleFullName}"
          type: DirectoryOrCreate
        name: "workspace"
      - name: hdfs-config
        configMap:
          name: hdfs-config
