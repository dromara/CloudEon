
# Windows开发指南
在搭建 `Cloudeon` 开发环境之前请确保你已经安装以下软件:

- Git
- JDK 1.8.x
- Mysql 5.7.x
- Maven

# 克隆代码库
通过你git管理工具下载git代码，以git cmd为例。
```
git clone https://github.com/dromara/CloudEon.git
```
# 启动后端
启动后端前要先做好以下准备：
1. 在mysql数据库新建数据库
2. 在CloudEon目录下新建log和work目录
3. 修改cloudeon-server/src/main/resources/目录下的application.properties配置文件
```
spring.datasource.url=jdbc:mysql://localhost:3306/cloudeon?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
spring.datasource.username=****
spring.datasource.password=****
```
修改数据库连接信息
```
udh.stack.load.path=F:\\JavaProjects\\CloudEon\\cloudeon-stack
udh.remote.script.path=F:\\JavaProjects\\CloudEon\\remote-script
udh.task.log=F:\\JavaProjects\\CloudEon\\log
udh.work.home=F:\\JavaProjects\\CloudEon\\work
```
修改udh配置信息
修改好配置之后，在Intellij IDEA找到启动类CloudEonApplication即可完成后端启动
# 启动前端
启动前端前要做好以下准备：

- node v16.13.2
- npm 8.1.2

## 第一次启动前端项目
1. 进入到cloudeon-ui目录进入命令行cmd，安装tyarn
   
    ```
    npm install yarn tyarn -g
    ```

2. 用tyarn安装依赖
   
    ```
    tyarn
    ```

3. 在cloudeon-ui/config/apiConfig.ts文件修改：
   
    ```bash
    devHost = "http://XX.XX.XX.XX:7700" // 修改成要代理的地址
    ```

4. 回到cmd，启动项目
   
    ```
    npm start
    ```

详情可参考/cloudeon-ui下的readme.md文件

截止目前，前后端都已成功启动运行，浏览器访问http://localhost:8000，并使用默认账户密码admin/admin即可完成登录。