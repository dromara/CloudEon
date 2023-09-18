# Dinky on  K8s
安装服务
![dinky1.png](../images/dinky/dinky1.png)
分配角色实例到指定节点安装
![dinky2.png](../images/dinky/dinky2.png)
修改初始化配置，需要填写mysql信息，数据库得提前自行准备好
![dinky3.png](../images/dinky/dinky3.png)
安装成功
![dinky5.png](../images/dinky/dinky5.png)
可以用在k8s集群上看到对应的pod
![dinky4.png](../images/dinky/dinky4.png)
打开web地址，默认密码是admin/admin
![dinky6.png](../images/dinky/dinky6.png)
在注册中心，Flink实例管理中创建flink集群地址
![dinky7.png](../images/dinky/dinky7.png)
在数据开发中创建目录，创建作业
![dinky8.png](../images/dinky/dinky8.png)
完成代码开发并在右侧选择执行模式，Flink集群,FlinkSQL环境并对任务进行保存并进行代码检查
![dinky9.png](../images/dinky/dinky9.png)
检查通过说明dinky与相关组件集成成功，目前dinky与hudi、kafka、hive、mysql-cdc、jdbc、es集成成功
![dinky10.png](../images/dinky/dinky10.png)
选择向flink提交任务
![dinky11.png](../images/dinky/dinky11.png)
可以看到flink作业已经正常在flink集群上启动
![dinky12.png](../images/dinky/dinky12.png)


Dinky支持Flink Yarn Application模式

在注册中心的集群管理中使用集群配置管理功能创建flink集群
![dinky13.png](../images/dinky/dinky13.png)
注意:
Hadoop 配置文件路径:/opt/edp/dinky/conf/  (固定)<br />
Flink 配置文件路径:/opt/flink-1.15.4/conf (固定)<br />
lib 路径:hdfs:///flink/lib    (默认，可自定义，但是需要将Flink的lib目录下的jar上传至自定义的hdfs目录)<br />
如需上传至自定义目录可进入dinky容器执行以下命令:<br />
hdfs dfs -put $FLINK_HOME/lib/*.jar  newpath
![dinky14.png](../images/dinky/dinky14.png)
