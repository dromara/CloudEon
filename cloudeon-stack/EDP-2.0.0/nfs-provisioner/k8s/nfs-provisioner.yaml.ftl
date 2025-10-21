apiVersion: helm.cattle.io/v1
kind: HelmChart
metadata:
  name: ${roleServiceFullName}
spec:
  targetNamespace: ${conf['nfs-provisioner.namespace']}
  <#-- 若目标命名空间不存在，则创建它；参考 https://docs.k3s.com.cn/helm#helmchart-field-definitions -->
  createNamespace: true
  chart: nfs-subdir-external-provisioner
  repo: ${conf['nfs-subdir-external-provisioner.helm.repo']}
  version: ${conf['nfs-subdir-external-provisioner.helm.version']}
  set:
<#macro property key value>
    ${key}: "${value}"
</#macro>
<#list confFiles['nfs-provisioner.conf'] as key, value>
    <@property key value/>
</#list>
  valuesContent: |-
    image:
      repository: ${conf['image.registry.proxy.k8s']}/sig-storage/nfs-subdir-external-provisioner
      pullPolicy: "IfNotPresent"
    nfs:
      server: ${conf['nfs.server']}
      path: ${conf['nfs.path']}
    storageClass:
      defaultClass: ${conf['storage.class.default-class']}
