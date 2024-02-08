# 数据持久化存储及多盘挂载

组件需要挂载的目录有2种，一种是组件要存储的数据目录，一般在支持在配置文件中指定路径，如果组件是做数据存储的，通常还支持多路径配置，如kafka、hdfs，这种目录存储的数据是重中之重，通常需要合理规划磁盘/目录。另外一种就是组件运行持久化目录，通常存放日志、状态文件（如pid）之类，相对没那么重要，通常对磁盘也不会有太高的要求。

为了屏蔽数据目录结构的复杂性，我们约定这两种目录在容器内为如下结构：

1. /workspace : 工作区目录，日志、状态文件等都应该放到这个目录
2. ["/data/1","/data/2"..."/data/x"] : 数据存储目录列表

为了简化，我们统一在所有需要持久化存储的组件中提供 `data.path.list`
配置项。即持久化挂载路径列表，逗号分隔。当为空时使用默认路径[全局参数global.persistence.basePath/组件名称]。
然后在k8s资源文件中，会将 `data.path.list`
的第一个路径的workspace子文件夹挂载成容器内的`/workspace`路径。如果组件有需要，再将`data.path.list`
的各个路径的data子文件夹挂载成容器内的`/data/数字`路径

实现这一方法的k8s资源文件的volumeMounts和volumes写法如下:
注意，使用如下写法，容器内至少会存在/workspace和/data/1目录

```ftl
<#if conf["data.path.list"]??&& conf["data.path.list"]?trim?has_content>
    <#assign primeDataPathList=conf["data.path.list"]?trim?split(",")>
<#else >
    <#assign primeDataPathList=[conf['global.persistence.basePath']]>
</#if>
<#assign dataPathList = []>
<#list primeDataPathList as dataPath>
    <#if dataPath?ends_with("/")>
      <#assign dataPathList = dataPathList + [dataPath+ roleFullName]>
    <#else>
      <#assign dataPathList = dataPathList + [dataPath+"/"+ roleFullName]>
    </#if>
</#list>
apiVersion: "apps/v1"
kind: "Deployment"
metadata:
  name: "${roleServiceFullName}"
spec:
  template:
    spec:
      containers:
      - name: "server"
        volumeMounts:
        - mountPath: "/workspace"
          name: "workspace"
<#list dataPathList as dataPath>
        - name: local-data-${dataPath?index+1}
          mountPath: /data/${dataPath?index+1}
</#list>
      volumes:
      - name: "workspace"
        hostPath:
          type: DirectoryOrCreate
          path: "${dataPathList[0]}/workspace"
<#list dataPathList as dataPath>
      - name: local-data-${dataPath?index+1}
        hostPath:
          path: ${dataPath}/data
          type: DirectoryOrCreate
</#list>

```

对应的组件配置文件写法如下：

```ftl
<#if conf["data.path.list"]??&& conf["data.path.list"]?trim?has_content>
    <#assign dataPathListSize=conf["data.path.list"]?trim?split(",")?size>
<#else >
    <#assign dataPathListSize=1>
</#if>
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
#
# Path to log files:
#
path:
    logs: /workspace/logs
    data:
<#list 1..dataPathListSize as dataPathIndex>
        - /data/${dataPathIndex}
</#list>

```
