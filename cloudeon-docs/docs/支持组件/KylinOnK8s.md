# Kylin on  K8s
安装Kylin服务
![img.png](../images/kylin1.png)
分配角色实例到指定节点安装
![img.png](../images/kylin2.png)
修改初始化配置，需要设置mysql配置
![img.png](../images/kylin3.png)

约等待几分钟，即可安装成功。
![img.png](../images/kylin4.png)
通过服务的web地址可以跳转kylin首页。
![img.png](../images/kylin5.png)
![img.png](../images/kylin6.png)
默认密码是ADMIN/KYLIN

可以用kubectl看到对应的pod

![img.png](../images/kylin7.png)