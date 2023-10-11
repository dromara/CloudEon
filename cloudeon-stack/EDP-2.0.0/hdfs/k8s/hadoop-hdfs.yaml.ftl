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
  <#case "hadoop-hdfs-journalnode">
        readinessProbe:
          tcpSocket:
            port: ${conf['journalnode.rpc-port']}
          failureThreshold: 3
          initialDelaySeconds: 60
          periodSeconds: 30
          successThreshold: 1
          timeoutSeconds: 15
        resources:
          requests:
            memory: "${conf['hadop.hdfs.jn.container.request.memory']}Mi"
            cpu: "${conf['hadop.hdfs.jn.container.request.cpu']}"
          limits:
            memory: "${conf['hadop.hdfs.jn.container.limit.memory']}Mi"
            cpu: "${conf['hadop.hdfs.jn.container.limit.cpu']}"
      <#break>
  <#case "hadoop-hdfs-namenode">
        readinessProbe:
          httpGet:
            path: "/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo&&user.name=hdfs"
            port: ${conf['namenode.http-port']}
            scheme: "HTTP"
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        resources:
          requests:
            memory: "${conf['hadop.hdfs.nn.container.request.memory']}Mi"
            cpu: "${conf['hadop.hdfs.nn.container.request.cpu']}"
          limits:
            memory: "${conf['hadop.hdfs.nn.container.limit.memory']}Mi"
            cpu: "${conf['hadop.hdfs.nn.container.limit.cpu']}"
    <#break>
  <#case "hadoop-hdfs-datanode">
        readinessProbe:
          exec:
            command:
            - "/bin/bash"
            - "-c"
            - "curl --fail --connect-timeout 15 --max-time 15 \"http://`hostname`:${conf['datanode.http-port']}/?user.name=hdfs\"\
            \n"
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        resources:
          requests:
            memory: "${conf['hadop.hdfs.dn.container.request.memory']}Mi"
            cpu: "${conf['hadop.hdfs.dn.container.request.cpu']}"
          limits:
            memory: "${conf['hadop.hdfs.dn.container.limit.memory']}Mi"
            cpu: "${conf['hadop.hdfs.dn.container.limit.cpu']}"
    <#break>
  <#case "hadoop-hdfs-httpfs">
        readinessProbe:
          exec:
            command:
            - "/bin/bash"
            - "-c"
            - "netstat -an | grep ${conf['httpfs.http-port']} | grep LISTEN >/dev/null"
          failureThreshold: 3
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        resources:
          requests:
            memory: "${conf['hadop.hdfs.httpfs.container.request.memory']}Mi"
            cpu: "${conf['hadop.hdfs.httpfs.container.request.cpu']}"
          limits:
            memory: "${conf['hadop.hdfs.httpfs.container.limit.memory']}Mi"
            cpu: "${conf['hadop.hdfs.httpfs.container.limit.cpu']}"
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
          name: hdfs-service-render
      - name: service-common
        configMap:
          name: hdfs-service-common
      - hostPath:
          path: "/opt/edp/${roleFullName}"
          type: DirectoryOrCreate
        name: "workspace"