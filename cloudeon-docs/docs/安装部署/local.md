

# 本地部署
## 环境准备

- JDK 1.8+
- MySql5.7+ (可选，内置H2)
- Cloudeon 安装包（cloudeon-assembly-*-release.zip)
- Kubernetes 1.21 +
## 文件结构
首先下载安装包，并且解压安装包。
```
unzip cloudeon-assembly-*-release.zip
```
解压之后的文件结构如下
```
├── bin               # 执行脚本目录
├── conf            # 配置文件目录
├── script          # 临时脚本
├── lib               # 项目依赖目录
├── stack            # 大数据服务安装包插件
└── LICENSE
```
## 启动应用
运行 bin 目录下的脚本来启动应用，Linux 用户使用 `bin/server.sh`，命令列表如下：
```
${CLOUDEON_HOME}/bin/server.sh start       # 启动
${CLOUDEON_HOME}/bin/server.sh stop        # 停止
${CLOUDEON_HOME}/bin/server.sh status      # 查看状态
${CLOUDEON_HOME}/bin/server.sh restart     # 重启
```
### 直接运行
安装包解压后，即可直接运行脚本启动应用。**需要注意的是，直接启动时使用的是内置的 H2 数据库作为应用数据库。**
启动之后通过 http://127.0.0.1:7700 地址访问应用主页，内置初始账户，用户名 admin 密码 admin
### 配置应用数据库 ( 非必须)
cloudeon 目前支持配置 MySQL 作为应用数据库；**需要 MySQL 5.7 及以上版本**。配置步骤如下：
创建数据库，指定数据库编码为 utf8
```
mysql> CREATE DATABASE `cloudeon` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
```
**注意：在初次连接时会自动初始化数据库**
**首次连接数据库(或者版本升级)时,建议使用一个权限较高的数据库账号登录(建议 root 账号)。因为首次连接会执行数据库初始化脚本，如果使用的数据库账号权限太低，会导致数据库初始化失败**
编辑 conf/application.properties 文件，将数据库信息配置修改
```properties
       
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
 # 数据库IP或域名
spring.datasource.url=jdbc:mysql://localhost:3306/cloudeon?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
# 用户名
spring.datasource.username=root
# 密码
spring.datasource.password=root
```

