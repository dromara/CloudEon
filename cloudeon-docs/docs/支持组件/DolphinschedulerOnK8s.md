# Dolphinscheduler on  K8s
## 介绍
支持Dolphinscheduler 多masater多worker模式在k8s上运行
## 操作
安装服务
![img.png](../images/ds-4.png)
分配角色实例到指定节点安装
![img.png](../images/ds-3.png)
修改初始化配置，需要手动调整mysql数据库相关配置
![img.png](../images/ds-5.png)
等待安装成功
![img.png](../images/ds-6.png)
安装成功后，可以进入服务详情，进行Doris服务的管理。
![图片.png](../images/ds-2.png)
![图片.png](../images/ds-1.png)

可以用kubectl看到对应的pod
![img.png](../images/ds-7.png)
