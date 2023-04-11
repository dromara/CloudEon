# 部署前提



本文介绍在部署 CloudEon前, 所需要准备的一些先决条件。


## Kubernetes环境准备（必须）
CloudEon需要一个可访问的Kubernetes集群，目前已知支持的版本是`1.21+` ，如果没有Kubernetes环境可以使用 [kubekey](https://github.com/kubesphere/kubekey) 快速搭建一个。
部分大数据服务也支持在k3s上部署，后续会逐渐全部适配，欢迎大家一起来完善。

## SSH服务准备（必须）
CloudEon需要访问Kubernetes集群中节点的SSH服务，所以必须保证网络可通

## 数据库环境准备（非必须）
CloudEon默认使用H2作为内置数据库，当然也支持Mysql作为数据库，可以通过修改application.properties文件进行配置