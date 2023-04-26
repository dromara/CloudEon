# HDFS on  K8s
## 介绍
支持HDFS HA模式在k8s上运行
## 操作
安装HDFS服务
![图片.png](../images/hdfs-1.png)

分配角色实例到指定节点安装
![图片.png](../images/hdfs-2.png)

修改初始化配置，一般不用调整
![图片.png](../images/hdfs-3.png)

等待安装成功
![图片.png](../images/hdfs-4.png)

安装成功后，可以进入服务详情，进行HDFS服务的管理。
![图片.png](../images/hdfs-5.png)

可以用kubectl看到对应的pod
![图片.png](../images/hdfs-6.png)

