# Yarn on  K8s
## 介绍
支持YARN HA模式在k8s上运行
## 操作
安装Yarn服务
![图片.png](../images/yarn-1.png)

分配角色实例到指定节点安装
![图片.png](../images/yarn-2.png)

修改初始化配置，一般不用调整
![图片.png](../images/yarn-3.png)

安装成功
![图片.png](../images/yarn-5.png)

可以用kubectl看到对应的pod
![图片.png](../images/yarn-6.png)