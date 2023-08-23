---
apiVersion: "apps/v1"
kind: "Deployment"
metadata:
  labels:
    name: "${roleServiceFullName}"
  name: "${roleServiceFullName}"
  namespace: "default"
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
                - "default"
              topologyKey: "kubernetes.io/hostname"
      hostPID: false
      hostNetwork: true
      containers:
        - args:
            - "/opt/edp/${service.serviceName}/conf/bootstrap-coordinator.sh"
          env:
            - name: "USER"
              value: "hadoop"
          image: "${dockerImage}"
          imagePullPolicy: "Always"
          readinessProbe:
            httpGet:
              path: /v1/info
              port: http
            initialDelaySeconds: 20
            periodSeconds: 10
            timeoutSeconds: 5
            failureThreshold: 6
            successThreshold: 1
          ports:
            - name: http
              containerPort: ${conf['coordinator.http.port']}
              protocol: TCP
          name: "${roleServiceFullName}"
          resources:
            requests: { }
            limits: { }
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

