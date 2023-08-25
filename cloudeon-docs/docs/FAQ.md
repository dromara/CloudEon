# FAQ

### 创建集群时，怎么获取kubeconfig

获取kubeconfig的常用方法有以下几种:

1. 在集群节点上直接获取。kubeconfig文件通常保存在管理节点的 ~/.kube/config 路径下。可以直接将这个文件拷贝到本地使用。

2. 使用kubectl config命令导出。在任意能访问集群的节点上运行:

```
kubectl config view --raw > kubeconfig.yaml
```

这会将当前的kubeconfig设置导出到kubeconfig.yaml文件中。

3. 从集群的管理界面获取。许多Kubernetes集群提供了图形界面管理,可以在这个界面直接下载kubeconfig文件。

4. 让集群管理员直接提供。如果你没有集群的直接访问权限,可以请集群管理员通过安全方式将kubeconfig文件提供给你。

5. 在云服务提供商控制台获取。在如AWS EKS这样的托管Kubernetes服务中,可以直接在控制台下载对应的kubeconfig。

6. 使用客户端证书身份验证。这种情况下不需要kubeconfig文件,而是使用客户端证书、私钥和CA证书直接访问API服务器。

获取到kubeconfig后,保管好该文件,并正确设置其文件权限,以防信息泄露。在使用kubectl等命令行工具时,可以通过--kubeconfig参数指向kubeconfig文件来使用它访问集群。

### 怎么去除节点污点
查看节点的污点： 使用以下命令查看节点上的污点信息：
```
kubectl describe node <node_name> | grep Taint
```
替换 <node_name> 为节点的名称。

移除污点： 如果你确定要移除节点上的污点，可以使用以下命令：

```
kubectl taint nodes <node_name> <taint_key>-
```
替换 <node_name> 为节点的名称，<taint_key> 为要移除的污点的键名。注意命令最后的 -，它表示移除该污点。

例如，如果要移除名为 `node-role.kubernetes.io/control-plane:NoSchedule` 的污点，可以运行：

```
kubectl taint nodes <node_name> node-role.kubernetes.io/control-plane:NoSchedule-
```
验证污点是否移除： 可以再次运行 `kubectl describe node <node_name> | grep Taint `命令来验证污点是否已经成功移除。