# 快速开始

## 安装

建议采用docker安装部署的方式快速完成服务运行，无需挂载外部配置，数据库使用默认的H2即可

## 创建集群和添加节点

参考docker部分的教程

## 安装global组件

global.monitor.type 选 NONE，即不启用监控，其他默认即可

## 安装zookeeper组件

保持默认配置即可

## 验证

```shell
# 进入zookeeper容器
kubectl -n=命名空间 exec -it pod名称 bash
# 进入zk命令终端
$ZOOKEEPER_HOME/bin/zkCli.sh  -server localhost:$ZK_CLIENT_PORT
# 执行zk命令测试功能
ls /
create /tmp1
ls /
delete /tmp1
ls /
quit
```