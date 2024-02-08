# 注入变量介绍

这部分介绍编写k8s、k8s-render、kube-prometheus-render、service-render等目录的ftl文件时可以注入使用的变量。

## 总体变量

| 变量名                 | 中文名             | 示例值                   | 说明                                                                                        |
|---------------------|-----------------|-----------------------|-------------------------------------------------------------------------------------------|
| clusterId           | 集群id            | 1                     |                                                                                           |
| stackId             | 使用的大数据组件套件id    | 2                     | 2对应 EDP-2.0.0                                                                             |
| namespace           | k8s命名空间         | namespace             | cloudeon                                                                                  |
| serviceFullName     | 组件全称名字          | yarn                  | 组件名称的k8s规范形式（转小写、用"-"替换"_"）                                                               |
| service             | 组件实体            | serviceInstanceEntity | 结构见下                                                                                      |
| serviceRoles        | 组件的角色列表         |                       | 结构见下                                                                                      |
| conf                | 组件的配置map        |                       | 包含当前组件和global组件的配置                                                                        |
| cloudeonURL         | cloudeon后端服务url | http://ip:7700        | ip为InetAddress.getLocalHost().getHostAddress()，端口为server.port                             |
| alertRules          | 组件的告警规则列表       |                       | 结构见下                                                                                      |
| confFiles           | 组件的自定义配置文件      |                       | 结构见下                                                                                      |
| super               | 特殊配置map         |                       | 用于给一些特殊组件提供特殊配置。目前只包含一个key：kube_config，并且需在程序的cloudeon.stackService.withKubeConfig中配置才会注入 |
| roleFullName        | 角色全称名字          |                       | 当任务是角色级别的时候才有效                                                                            |
| roleServiceFullName | 组件角色全称名字        |                       | 当任务是角色级别的时候才有效，是roleFullName和serviceFullName的拼接                                           |
| roleNodeCnt         | 角色的部署节点数        |                       | 当任务是角色级别的时候才有效                                                                            |
| dependencies        | 组件的依赖组件map      |                       | 当任务是角色级别的时候才有效，结构见下                                                                       |

## service

这里的service对应的是组件实例，即 ce_service_instance 表。
而ce_stack_service表包含的是组件原始信息，对应info文件的原始信息。
可以理解成一个是类，一个是实例的关系。

| 变量名                          | 中文名       | 示例值           | 说明            |
|------------------------------|-----------|---------------|---------------|
| id                           | 组件实例id    | 17            |               |
| clusterId                    | 集群id      | 1             |               |
| serviceName                  | 组件名称      | yarn          | k8s规范形式       |
| label                        | 组件label   | YARN          | 在info文件中配置    |
| serviceState                 | 组件状态      | 服务已启动         | 见ServiceState |
| stackServiceId               | 组件原始信息id  | 25            |               |
| dependenceServiceInstanceIds | 依赖的组件实例id | 5,6           |               |
| createTime                   | 组件实例创建时间  | 1705834061832 |               |
| updateTime                   | 组件实例修改时间  | 1705834061832 |               |

## serviceRoles

map结构，key为角色名称，value为角色实例列表，其中每个角色实例包含id、hostname、roleName信息
示例如下：

```json
{
  "serviceRoles": {
    "HDFS_ZKFC_FORMAT": [
      {
        "id": 10,
        "hostname": "host1",
        "roleName": "HDFS_ZKFC_FORMAT"
      }
    ],
    "HDFS_JOURNALNODE": [
      {
        "id": 11,
        "hostname": "host1",
        "roleName": "HDFS_JOURNALNODE"
      },
      {
        "id": 12,
        "hostname": "host2",
        "roleName": "HDFS_JOURNALNODE"
      },
      {
        "id": 13,
        "hostname": "host3",
        "roleName": "HDFS_JOURNALNODE"
      }
    ]
  }
}
```

## confFiles

map结构，key为配置文件名称，value为文件下的自定义配置map

示例如下：

```json
{
  "confFiles": {
    "httpfs-site.xml": {},
    "hdfs-site.xml": {
      "dfs.permissions.superusergroup": "supergroup",
      "dfs.data.transfer.protection": "authentication",
      "dfs.client.read.shortcircuit": "false",
      "dfs.datanode.handler.count": "30",
      "dfs.client.socket-timeout": "120000",
      "dfs.datanode.failed.volumes.tolerated": "0",
      "dfs.datanode.data.dir.perm": "755",
      "dfs.namenode.handler.count": "100",
      "dfs.namenode.acls.enabled": "true"
    },
    "core-site.xml": {
      "fs.trash.interval": "1440",
      "dfs.ha.fencing.methods": "shell(/bin/true)",
      "hadoop.http.staticuser.user": "hdfs"
    }
  }
}
```

## dependencies

map结构，key为依赖的组件名，
value为map结构，包含service（结构见上）、serviceRoles（结构见上）、conf（不额外添加global的配置项）

示例如下：

```json
{
  "dependencies": {
    "GLOBAL": {
      "conf": {
        "global.imagePullPolicy": "IfNotPresent",
        "global.kube-prometheus.external.grafana.url": "http://localhost:30902",
        "global.persistence.basePath": "/data/edp",
        "global.user.list": "user001,group001;user002,group002",
        "global.monitor.type": "INTERNAL_HELM_KUBE_PROMETHEUS"
      },
      "serviceRoles": {},
      "service": {
        "id": 1,
        "clusterId": 1,
        "serviceName": "global",
        "label": "Global",
        "serviceState": "服务已启动",
        "updateTime": 1704940367739,
        "createTime": 1704940367739,
        "stackServiceId": 21
      }
    },
    "ZOOKEEPER": {
      "conf": {
        "sasl.oauth2.enabled": "true",
        "4lw.commands.whitelist": "*",
        "zookeeper.container.limit.cpu": "1.0",
        "syncLimit": "5",
        "zookeeper.container.limit.memory": "2048",
        "zookeeper.jmxremote.port": "19911",
        "zookeeper.peer.communicate.port": "12888",
        "zookeeper.leader.elect.port": "13888",
        "zookeeper.container.request.memory": "1024",
        "znode.container.checkIntervalMs": "1000",
        "zookeeper.client.port": "12181",
        "serverImage": "registry.cn-guangzhou.aliyuncs.com/bigdata200/zookeeper:3.7.1",
        "tickTime": "9000",
        "initLimit": "10",
        "zookeeper.container.request.cpu": "0.2",
        "extendedTypesEnabled": "false",
        "zookeeper.jvm.memory.percentage": "75",
        "maxClientCnxns": "0",
        "zookeeper.metrics.port": "12187"
      },
      "serviceRoles": {
        "ZOOKEEPER_SERVER": [
          {
            "id": 7,
            "hostname": "ip12",
            "roleName": "ZOOKEEPER_SERVER"
          },
          {
            "id": 8,
            "hostname": "ip13",
            "roleName": "ZOOKEEPER_SERVER"
          },
          {
            "id": 9,
            "hostname": "ip14",
            "roleName": "ZOOKEEPER_SERVER"
          }
        ]
      },
      "service": {
        "id": 5,
        "clusterId": 1,
        "serviceName": "zookeeper",
        "label": "ZooKeeper",
        "serviceState": "服务已启动",
        "updateTime": 1704964777665,
        "createTime": 1704964777665,
        "dependenceServiceInstanceIds": "1",
        "stackServiceId": 26
      }
    }
  }
}

```

## alertRules

告警规则列表，每个告警规则实体有如下字段

| 字段名              | 中文名                                 | 示例                                      | 说明 |
|------------------|-------------------------------------|-----------------------------------------|----|
| id               |                                     | 1                                       |    |
| clusterId        | 集群id                                | 1                                       |    |
| ruleName         | 规则名称                                | ZookeeperServer存活数检测                    |    |
| alertLevel       | 告警级别                                | 异常级别                                    |    |
| promql           | 触发告警的Prometheus表达式                  |                                         | 见下 |
| stackServiceName | 告警规则所属组件名                           | ZOOKEEPER                               |    |
| stackRoleName    | 告警规则所属组件的角色名                        | ZOOKEEPER_SERVER                        |    |
| alertInfo        | 告警信息，{{$value}}是alertmanager的变量取值写法 | ZookeeperServer发生宕机，当前存活实例数为：{{$value}} |    |
| alertAdvice      | 告警建议                                | Zookeeper发生宕机，请检查日志/执行重启                |    |
| updateTime       | 更新时间                                | 1706195891000                           |    |

告警表达式promql是核心，为了增强其能力，这里添加#{{freemarker表达式}}#语法，用于注入cloudeon的变量。该功能依赖alert-rule.yaml.ftl中的executeDynamicCode方法。
使用如下：
"sum(up{job=\"metrics-zookeeper\"})<#{{${serviceRoles['ZOOKEEPER_SERVER']?size}}}#"
当zookeeper存活进程数低于目标数时告警。目标数根据实际配置注入。

另外，之所以不使用 up=0做进程检查是因为进程消失一段时间后对应的up记录也会消失，直接用up做告警不可靠

示例：

```json
{
  "alertRules": [
    {
      "id": 1,
      "clusterId": 1,
      "ruleName": "ZookeeperServer存活数检测",
      "alertLevel": "异常级别",
      "promql": "sum(up{job=\"metrics-zookeeper\"})<#{{${serviceRoles['ZOOKEEPER_SERVER']?size}}}#",
      "stackServiceName": "ZOOKEEPER",
      "stackRoleName": "ZOOKEEPER_SERVER",
      "alertInfo": "ZookeeperServer发生宕机，当前存活实例数为：{{$value}}",
      "alertAdvice": "Zookeeper发生宕机，请检查日志/执行重启",
      "updateTime": 1706195891000
    }
  ]
}
```
