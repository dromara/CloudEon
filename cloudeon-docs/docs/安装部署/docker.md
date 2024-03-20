# Docker部署
如果你本地已经安装了 docker，执行以下命令可以一键安装：

```shell
docker run -d --name cloudeon -p 7700:7700 --rm registry.cn-guangzhou.aliyuncs.com/bigdata200/cloudeon:v2.0.0-beta
```

tag对应版本号，需根据实际情况修改

镜像启动成功后，在浏览器中访问 http://docker_ip:7700 进入登录页。
镜像中提供初始账户，用户名 admin 密码 admin

### 新增集群

点击右上角"新增集群"，在弹窗中输入/选择以下配置

- 集群名称：自定义即可
- kubernetes命名空间：需提前创建，参考命令为 `kubectl create ns cloudeon`
- 框架：EDP-2.0.0
- kubeConfig：k8s配置信息，获取命令可以参考`cat $HOME/.kube/config`或`kubectl config view --minify --raw`

点击确定，成功添加集群。

### 添加节点

点击以上步骤添加的集群，然后点击右上角"新增节点",选择k8s节点

### 添加服务

点击左侧列表服务，然后点击右上角"新增服务"，选择要按照的服务组件，点击下一步，自定义组件的节点分布配置，然后点击下一步，开始安装服务。

遇到问题优先在指令界面查看日志，未能解决则需使用k8s相关命令进一步排查

k8s有以下常用指令，也可以用一些k8s可视化运维工具：

```shell
# 获取命名空间下所有deployment
kubectl -n=命名空间 get deployment
# 获取命名空间下所有pod
kubectl -n=命名空间 get pod
# 查看pod状态及具体信息：通常是环境问题，比方说因镜像拉取失败导致无法运行
kubectl -n=命名空间 describe pod ${pod_name}
# 追踪pod日志：通常用于发现启动脚本报错或组件自身报错
kubectl -n=命名空间 logs -f --tail=100 ${pod_name}
# 进入pod内部进行排查
kubectl -n=命名空间 exec -it ${pod_name} bash

```

## 配置应用数据库

application.properties文件中包含H2和MySQL作为应用程序数据库的配置，其中默认使用的是H2，意味着程序不用额外准备数据库即可启动。
MySQL部分则是注释掉的。如果将 Cloudeon 用于生产环境，建议使用 MySQL 作为应用程序数据库，把H2部分注释掉，并修改MySQL部分的连接配置即可。

application.properties文件的获取和使用见下一部分

默认相关配置应如下，请以实际配置为准：

```properties
# h2
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:file:${cloudeon.home.path}/cloudeon;MODE=MySQL;DATABASE_TO_LOWER=TRUE
spring.datasource.username=root
spring.datasource.password=eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT
# mysql
#spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#spring.datasource.url=jdbc:mysql://localhost:3306/cloudeon?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
#spring.datasource.username=root
#spring.datasource.password=eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT
```

## 配置文件的获取和挂载使用

因不同版本配置文件可能不同，建议直接从镜像中获取application.properties配置文件，参考命令如下：

```shell
# 镜像名
image=registry.cn-guangzhou.aliyuncs.com/bigdata200/cloudeon:v2.0.0-beta
# 存放配置文件的外部路径
conf_path_dir=/opt/cloudeon
# 运行临时容器把配置文件复制到外部
docker run --rm --entrypoint /bin/bash -v $conf_path_dir:/data/conf $image -c "cp  /opt/cloudeon/conf/application.properties /data/conf/"

```

这样就可以在 $conf_path_dir 目录下找到 application.properties 并进行修改，然后再正式挂载使用

```shell
# 镜像名
image=registry.cn-guangzhou.aliyuncs.com/bigdata200/cloudeon:v2.0.0-beta
# 存放配置文件的外部路径
conf_path_dir=/opt/cloudeon
# 正式运行
docker run -d --name cloudeon -v $conf_path_dir/application.properties:/opt/cloudeon/conf/application.properties -p 7700:7700 $image

```