apiVersion: apps/v1
kind: Deployment
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
      hostNetwork: true
      containers:
        - name: "${roleServiceFullName}"
          image: "${conf['serverImage']}"
          imagePullPolicy: "${conf['global.imagePullPolicy']}"
          command: ["helm-controller"]
          args: ["--kubeconfig", "/root/.kube/config"]
          volumeMounts:
          - name: kube-config
            mountPath: /root/.kube/config
            subPath: config
      volumes:
        - name: kube-config
          configMap:
            name: helm-controller-kube-config
