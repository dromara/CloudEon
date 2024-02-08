# Docker部署
如果你本地已经安装了 docker，执行以下命令可以一键安装：

```shell
 docker run  -p 7700:7700  --name cloudeon --rm registry.cn-hangzhou.aliyuncs.com/udh/cloudeon:dev
```
其中`dev`对应的是dev分支，如果改成`v1.1.0`则是1.1.0版本。

镜像启动成功后，在浏览器中访问 http://docker_ip:7700 进入登录页。镜像中提供初始账户，用户名 admin 密码 admin

### 新增集群

点击右上角"新增集群"，在弹窗中输入集群名称（自定义），选择框架(EDP-1.0.0)，将k8s配置信息($HOME/.kube/config)添加到kubeConfig文本框内,点击确定，成功添加集群。

### 添加节点

点击以上步骤添加的集群，然后点击右上角"新增节点",选择k8s节点

### 添加服务

点击左侧列表服务，然后点击右上角"新增服务"，选择要按照的服务组件，点击下一步，自定义组件的节点分布配置，然后点击下一步，开始安装服务。

注：如果进度一直卡在等待deployment成功，可通过用`kubectl get pod `以及`kubectl get deployment`查看组件安装部署状态。

如遇错误现象可通过`kubectl describe pod ${pod_name}`命令查看具体错误信息。




## 配置应用数据库

在默认情况下，Cloudeon 使用内置的 H2 作为应用程序数据库，意味着你不用额外准备数据库即可启动。 如果将 Cloudeon 用于生产环境，建议使用 MySQL 作为应用程序数据库。配置步骤如下：
新建一个名为 application.properties 的空文件，将以下内容填写完整，然后粘贴到到文件中
```properties
# Http server port
server.port=7700

spring.main.banner-mode=log

# db
spring.jpa.open-in-view=false
spring.jpa.hibernate.ddl-auto=none
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/cloudeon?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=root


####### flyway properties #######
spring.flyway.enabled=true
spring.flyway.clean-disabled=true
spring.flyway.validate-on-migrate=true

# Configuration for uploading files.
spring.servlet.multipart.enabled=true
spring.servlet.multipart.file-size-threshold=0
spring.servlet.multipart.max-file-size=209715200
spring.servlet.multipart.max-request-size=209715200

# temporary skip circular references check
spring.main.allow-circular-references=true

####### cloudeon properties #######
cloudeon.stack.load.path=${cloudeon.home.path}/stack
cloudeon.remote.script.path=${cloudeon.home.path}/script
cloudeon.task.log=${cloudeon.home.path}/log
cloudeon.work.home=${cloudeon.home.path}/work

logging.config=${cloudeon.home.path}/conf/logback.xml

```
## 配置文件挂载
运行以下命令，使用新建的 application.properties 配置启动镜像
```
docker run -d --name cloudeon -v your_path/application.properties:/usr/local/cloudeon/cloudeon-assembly/conf/application.properties -p 7700:7700 registry.cn-hangzhou.aliyuncs.com/udh/cloudeon:dev
```
