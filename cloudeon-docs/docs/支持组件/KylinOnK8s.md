# Kylin on  K8s
安装Kylin服务
![img.png](../images/kylin1.png)
分配角色实例到指定节点安装
![img.png](../images/kylin2.png)
修改初始化配置，需要设置mysql配置
![img.png](../images/kylin3.png)

约等待10分钟，即可安装成功。kylin默认启动前会做多处检查，所以时间会比较长。

可以用kubectl看到对应的pod
