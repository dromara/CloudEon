apiVersion: helm.cattle.io/v1
kind: HelmChart
metadata:
  name: ${roleFullName}
  <#--
  name: ${roleServiceFullName}
  此处 metadata.namespace 是指当前的 HelmChart 资源本身所位于的命名空间（它必须已存在），并不一定是图表资源将部署到的命名空间（spec.targetNamespace），该命名空间是在同一清单中创建的。如果您希望将 HelmChart 资源与它们部署的资源分开，这将很有用。
  namespace: ${conf['open-metadata-dependencies.namespace']}
   -->
spec:
  targetNamespace: ${conf['open-metadata-dependencies.namespace']}
  createNamespace: true
  chart: openmetadata-dependencies
  repo: ${conf['open-metadata-dependencies.helm.repo']}
  version: ${conf['open-metadata-dependencies.helm.version']}
  valuesContent: |-
    mysql:
      enabled: false
      image:
        registry: ${conf['image.registry.proxy.docker']}
        repository: bitnami/mysql
        pullPolicy: "IfNotPresent"
      global:
        storageClass: "${conf['storage.class']}"
    opensearch:
      global:
        dockerRegistry: ${conf['image.registry.proxy.docker']}
      image:
        repository: "opensearchproject/opensearch"
        pullPolicy: "IfNotPresent"
      persistence:
        image: library/busybox
        imageTag: latest
        storageClass: "${conf['storage.class']}"
    airflow:
      airflow:
        image:
          repository: "${conf['image.registry.proxy.docker']}/openmetadata/ingestion"
          pullPolicy: "IfNotPresent"
      externalDatabase:
        type: mysql
        host: ${conf['airflow.external.db.host']}
        port: ${conf['airflow.external.db.port']}
        database: airflow_db
        user: airflow_user
        passwordSecret: airflow-mysql-secrets
        passwordSecretKey: airflow-mysql-password
      dags:
        persistence:
          storageClass: "${conf['storage.class']}"
      logs:
        persistence:
          storageClass: "${conf['storage.class']}"

    extraVolumes:
      - name: timezone-volume
        configMap:
          name: localtime

    extraVolumeMounts:
      - name: timezone-volume
        mountPath: /usr/share/zoneinfo/Asia
        readOnly: true
