<#-- 指定要创建的命名空间，避免下面的 Secret/ConfigMap，及后续的 service 等资源创建失败 -->
apiVersion: v1
kind: Namespace
metadata:
  name: ${conf["open-metadata-dependencies.namespace"]}

---
apiVersion: v1
kind: Secret
metadata:
  name: airflow-mysql-secrets
  namespace: ${conf["open-metadata-dependencies.namespace"]}
type: Opaque
data:
  airflow-mysql-password: ${conf["airflow.mysql.password"]}

---
apiVersion: v1
kind: Secret
type: Opaque
metadata:
  name: airflow-secrets
  namespace: ${conf["open-metadata-dependencies.namespace"]}
data:
  openmetadata-airflow-password: ${conf["openmetadata.airflow.password"]}

---
apiVersion: v1
kind: Secret
metadata:
  name: mysql-secrets
  namespace: ${conf["open-metadata-dependencies.namespace"]}
type: Opaque
data:
  openmetadata-mysql-password: ${conf["openmetadata.mysql.password"]}

---
apiVersion: v1
kind: ConfigMap
metadata:
  name: localtime
  namespace: ${conf["open-metadata-dependencies.namespace"]}
binaryData:
  Shanghai: VFppZjIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAdAAAAAwAAAAyAAAAAoJeigKF5BPDIWV6AyQn5cMnTvQDLBYrwy3xAANI7PvDTi3uA1EKt8NVFIgDWTL/w1zy/ANgGZnDZHfKA2UF88B66UiAfaZuQIH6EoCFJfZAiZ6EgIylfkCRHgyAlEnwQJidlICbyXhAoB0cgKNJAEAIBAgECAQIBAgECAQIBAgECAQIBAgECAQIBAgECAABx1wAAAAB+kAEEAABwgAAITE1UAENEVABDU1QAVFppZjIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAdAAAAAwAAAAz/////fjZDKf////+gl6KA/////6F5BPD/////yFlegP/////JCflw/////8nTvQD/////ywWK8P/////LfEAA/////9I7PvD/////04t7gP/////UQq3w/////9VFIgD/////1ky/8P/////XPL8A/////9gGZnD/////2R3ygP/////ZQXzwAAAAAB66UiAAAAAAH2mbkAAAAAAgfoSgAAAAACFJfZAAAAAAImehIAAAAAAjKV+QAAAAACRHgyAAAAAAJRJ8EAAAAAAmJ2UgAAAAACbyXhAAAAAAKAdHIAAAAAAo0kAQAgECAQIBAgECAQIBAgECAQIBAgECAQIBAgECAQIAAHHXAAAAAH6QAQQAAHCAAAhMTVQAQ0RUAENTVAAKQ1NULTgK
