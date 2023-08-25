# Trino on  K8s
## 介绍
支持Trino在k8s上运行，默认集成hive metastore，支持hive表查询
## 操作
安装Trino服务


分配角色实例到指定节点安装


修改初始化配置，一般不用调整


等待安装成功


安装成功后，可以进入服务详情，进行Trino服务的管理。



可以用kubectl看到对应的pod


使用trino cli连接trino服务，可以看到hive表
![img.png](img.png)