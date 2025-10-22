apiVersion: helm.cattle.io/v1
kind: HelmChart
metadata:
  name: ${roleServiceFullName}
spec:
  targetNamespace: ${conf['open-metadata.namespace']}
  createNamespace: true
  chart: openmetadata
  repo: ${conf['open-metadata.helm.repo']}
  version: ${conf['openmetadata.helm.version']}
  valuesContent: |-
    image:
      repository: ${conf['image.registry.proxy.docker']}/openmetadata/server
      pullPolicy: "IfNotPresent"
    <#-- 保证部署到配置页面上所选定的 K8s 节点 -->
    nodeSelector:
      ${roleServiceFullName}: "true"
    openmetadata:
      config:
        database:
          host: ${conf['database.sql.endpoint']}
          port: ${conf['database.port']}
          driverClass: ${conf['database.driver.class']}
          dbScheme: ${conf['database.db.scheme']}
          databaseName: ${conf['database.name']}
          auth:
            username: ${conf['database.username']}
            password:
              secretRef: mysql-secrets
              secretKey: openmetadata-mysql-password
          dbParams: "allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai"
    extraEnvs:
      - name: TZ
        value: "Asia/Shanghai"

    extraVolumes:
      - name: timezone-volume
        configMap:
          name: localtime

    extraVolumeMounts:
      - name: timezone-volume
        mountPath: /usr/share/zoneinfo/Asia
        readOnly: true
