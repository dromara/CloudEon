## 介绍
multipass是一个命令工具，可以支持linux和mac还有windows上快速创建Ubuntu虚拟机。
![图片.png](https://cdn.nlark.com/yuque/0/2022/png/637656/1670402676428-2ea3ff4d-1129-4c83-9bf1-c53da66f7b53.png#averageHue=%23f7f7f5&clientId=u65d8090e-95ca-4&from=paste&height=901&id=uc0e13bbb&name=%E5%9B%BE%E7%89%87.png&originHeight=901&originWidth=1555&originalType=binary&ratio=1&rotation=0&showTitle=false&size=212925&status=done&style=none&taskId=ud28a6416-b793-4e1a-9b48-eda4c3b8b17&title=&width=1555)
## 使用
### 下载multipass
下载到mac本地：
[https://multipass.run/install](https://multipass.run/install)

### 启动虚拟机bar
mac上查看镜像
![图片.png](https://cdn.nlark.com/yuque/0/2022/png/637656/1670403346585-dd6b69db-c848-4438-bdbd-36f6d8194538.png#averageHue=%23090909&clientId=ubfd36495-3fd1-4&from=paste&height=264&id=u51f7b4d0&name=%E5%9B%BE%E7%89%87.png&originHeight=264&originWidth=1894&originalType=binary&ratio=1&rotation=0&showTitle=false&size=62693&status=done&style=none&taskId=u418805ab-848d-480c-a17c-e1c028dbd6f&title=&width=1894)
mac上启动一个Ubuntu虚拟机，并命名为foo，使用bionic镜像。可以添加参数--cpus，--disk，--mem控制虚拟机资源。也可以通过 --mount将mac本机的目录挂载到虚拟机里的某个目录。
```shell
multipass launch --name bar bionic
```
在mac上启动成功后，可以看到对应的ip。
![图片.png](https://cdn.nlark.com/yuque/0/2022/png/637656/1670403845514-a3326801-0ce5-4c6a-aa6d-bd25808124ab.png#averageHue=%23070707&clientId=ubfd36495-3fd1-4&from=paste&height=100&id=uf5e788ba&name=%E5%9B%BE%E7%89%87.png&originHeight=100&originWidth=911&originalType=binary&ratio=1&rotation=0&showTitle=false&size=14326&status=done&style=none&taskId=ua4363e7b-7341-4f70-8fd9-9961b5f2002&title=&width=911)
### 配置虚拟机bar支持ssh密码访问
mac上执行命令，进入虚拟机中
```shell
multipass shell bar
```
![图片.png](https://cdn.nlark.com/yuque/0/2022/png/637656/1670403915338-7aedb44c-106a-472c-9b63-d5f1c9b79bd9.png#averageHue=%23080808&clientId=ubfd36495-3fd1-4&from=paste&height=651&id=u96a3eee8&name=%E5%9B%BE%E7%89%87.png&originHeight=651&originWidth=1051&originalType=binary&ratio=1&rotation=0&showTitle=false&size=90084&status=done&style=none&taskId=u7a408e35-64fb-4473-8d29-a756f7f922d&title=&width=1051)可以看到默认账号是ubuntu。
设置默认账号的密码为ubuntu
```shell
ubuntu@bar:~$ sudo passwd ubuntu
```
默认ssh是关闭用密码登录的，我们可以调整一下。
```shell
ubuntu@bar:~$ sudo vim /etc/ssh/sshd_config
ubuntu@bar:~$ sudo service sshd restart
```
![图片.png](https://cdn.nlark.com/yuque/0/2022/png/637656/1670404100734-fe10b0af-11c3-43a6-82ed-8a24ee9dd1cc.png#averageHue=%23020100&clientId=ubfd36495-3fd1-4&from=paste&height=223&id=uf8872d13&name=%E5%9B%BE%E7%89%87.png&originHeight=223&originWidth=878&originalType=binary&ratio=1&rotation=0&showTitle=false&size=34595&status=done&style=none&taskId=u01bad189-61da-4b05-8dbd-2de9ca50b3f&title=&width=878)在mac上用finalshell登录一下虚拟机bar。
![图片.png](https://cdn.nlark.com/yuque/0/2022/png/637656/1670404191990-409a1a60-f4bf-4dc0-ab26-c587b694d721.png#averageHue=%230b3857&clientId=ubfd36495-3fd1-4&from=paste&height=674&id=u45df7850&name=%E5%9B%BE%E7%89%87.png&originHeight=674&originWidth=1121&originalType=binary&ratio=1&rotation=0&showTitle=false&size=135225&status=done&style=none&taskId=ub3a5ecfb-0aa2-4bb5-abea-77a5918045e&title=&width=1121)
### 删掉虚拟机bar
在mac上删除虚拟机
```shell
multipass delete foo                                   
multipass purge          
multipass list
```

## 高级使用
### mac上查看日志
/Library/Logs/Multipass
