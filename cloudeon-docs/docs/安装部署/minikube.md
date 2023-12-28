# minikube部署轻量kubernetes
## 1. 简介

本文介绍使用minikube部署轻量kubernetes，用于CloudEon测试环境搭建。

## 2. 环境准备
安装minikube、vmware。
```text
注：不能使用docker作为虚拟节点驱动，因为无法直接访问节点ip和端口。
```

## 3. 部署kubernetes环境
```shell
minikube start --vm-driver=vmware --nodes 3
```

## 4. 设置所有节点root远程登录密码
### 4.1 查看kubernetes节点列表
```shell
minikube node list
```
```text
minikube	192.168.142.135
minikube-m02	192.168.142.136
minikube-m03	192.168.142.137
```
### 4.2 登录kubernetes子节点
```shell
minikube ssh -n minikube-m03
```
### 4.3 设置root密码
```shell
sudo passwd
```
### 4.4 允许root用户远程登录
```shell
sudo vi /etc/ssh/sshd_config
```
修改
```shell
PermitRootLogin yes
```
重启sshd服务
```shell
sudo systemctl restart sshd
```
### 4.5 验证root用户远程登录
```shell
ssh root@minikube-m03
```
## 5. 获取kubernetes的kubeConfig
```shell
cat ~/.kube/config
```