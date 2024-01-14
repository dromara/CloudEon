apiVersion: v1
kind: ConfigMap
metadata:
  name: helm-controller-kube-config
data:
  config: |
<#-- 缩进 kubeConfig -->
${super.kube_config?split('\n')?map(item -> '    ' + item)?join('\n')}
