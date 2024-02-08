# 总览

## 总流程

### 1.拉取源码

https://github.com/dromara/CloudEon.git

### 2.新建组件目录并编写内容

如`CloudEon\cloudeon-stack\EDP-2.0.0\组件服务名`
这个过程可以参考其他组件的实现，并结合文档进行编写

### 3.重启cloudeon服务

cloudeon启动时会自动加载组件

### 4.编写文档

    1. CloudEon\cloudeon-docs\docs\组件说明\组件名.md
    2. 在CloudEon\cloudeon-docs\docs\mkdocs.yml的nav添加对应组件说明文件路径

## 组件文件结构

一个完整的组件目录结构如下：

- docker

  组件所需的镜像来源文件，通常是Dockerfile和对应的build.sh

- icons

  名称固定为app.png的组件logo图片，大小应为200*200像素，logo的提取可以使用这个网站：https://icon.horse

- k8s

  组件角色的k8s资源文件模板，通常是yaml.ftl后缀，将注入模型变量然后根据freemarker语法生成k8s资源文件。虽然k8s支持一个文件包含多个资源（如同时包含Deployment和ConfigMap），但不建议这样做，应该把ConfigMap、Service放到k8s-common、k8s-render下。因为这个目录里的资源不仅仅执行apply，还可能会进行识别名称、状态检查、日志跟踪等操作。
  注意：不同角色可使用同一模板文件，将使用文件名和角色进行前缀匹配，匹配度最高的文件作为该角色的模板文件。例如有hadoop-hdfs.yaml.ftl和hadoop-hdfs-zkfc-format.yaml.ftl两个模板文件，hadoop-hdfs-journalnode、hadoop-hdfs-namenode、hadoop-hdfs-datanode都会匹配使用hadoop-hdfs.yaml.ftl（hadoop-hdfs-zkfc-format不能被作为这几个角色的前缀，所以被筛选掉），而hadoop-hdfs-zkfc-format就会匹配到hadoop-hdfs-zkfc-format.yaml.ftl（hadoop-hdfs.yaml.ftl也符合条件，但匹配长度较短）

- k8s-common

  直接使用kubectl apply -f 的k8s资源文件的文件夹。应用较少。

- k8s-render

  k8s资源文件的模板文件，会注入模型渲染后发布，生成可用的k8s资源文件，然后进行apply。

- kube-prometheus-render

  kube-prometheus相关k8s资源的模板文件目录。本质上和k8s-render一样。但如果在全局参数中设置监控方案为“不启用”则不会处理此目录。因为这个目录下的资源通常是kube-prometheus-stack的自定义资源，如ServiceMonitor、PrometheusRule之类。

- service-common

  此文件夹下的文件会不经过渲染直接放到根据其组件命名的configmap中。通常放启动脚本、检活脚本之类不需要渲染的文件，以及模型变量转换得到的value.json文件。
  configmap的名称为：stackServiceName + "-service-common"
  。其中stackServiceName为service-info.yaml的name的k8s规范形式（小写、'-'代替'_'）。
  补充说明见下

- service-render

  此文件夹存放freemarker模板文件，此文件夹下的文件会不经过渲染直接放到根据其组件命名的configmap中。通常放组件需要渲染的配置文件，如zoo.cfg.ftl等。
  configmap的名称为：stackServiceName + "-service-render"
  。其中stackServiceName为service-info.yaml的name的k8s规范形式（小写、'-'代替'_'）。
  补充说明见下

- service-info.yaml

  组件服务核心配置文件
  补充说明见下

- alert-rule.yaml

  默认告警规则列表

注意，以上只有service-info.yaml和icons是必需的。

其中k8s对应组件角色启停任务，k8s-common、k8s-render、kube-prometheus-render、service-common、service-render对应组件配置启停任务

## service-common和service-render

组件的配置文件（脚本）的注入是核心。因为组件的配置文件通常是与实例相关的，如很多配置文件中都需要填写当前实例的hostname、ip。所以同一角色的不同实例的配置文件必然是不同的。但不同实例又同属一个deployment之类的部署资源。所以解决思路有2种。

1. 给每个实例单独生成一份配置文件，放到一个configmap里面，容器启动时再根据当前节点信息筛选自身需要的配置文件进行使用
2. 把模板和渲染所需的模型变量都传递到容器里面，容器启动时再结合当前节点信息（环境变量）进行渲染，得到真正可用的配置文件，如zoo.cfg。

最终选择思路2，感觉更优雅，思维上更直接，至于渲染操作过程的复杂性，可以借助global全局启动脚本（见下）将其隐藏掉，只需按约定编写k8s资源文件即可。

要渲染的模板文件通过service-render注入，模型变量则作为value.json通过service-common注入

约定规范如下：
其中volumes引用了global-service-common、global-render-config、组件名-service-render、组件名-service-common。组件名是service-info.yaml的name的k8s规范形式。这几个cm会分别挂载到/opt/global/bootstrap.sh、/opt/global/10.render、/opt/service-render、/opt/service-common文件/目录下。

容器的command和args按如下格式编写，args的第一行是`/bin/bash /opt/global/bootstrap.sh`
，用于加载全局启动脚本，由其自动加载global-render-config的bootstrap.sh，将$RENDER_TPL_DIR目录下ftl后缀的文件根据$RENDER_MODEL渲染后发布到$RENDER_TPL_DIR-output目录，非ftl后缀的文件则直接从$RENDER_TPL_DIR目录复制到$RENDER_TPL_DIR-output目录。

以上除了组件名称，其他的都应该保持默认。

这样，在组件真正的启动脚本里（通常是/opt/service-common/bootstrap.sh），就可以直接在`/opt/service-common`
和`/opt/service-render-output`
得到想要的配置文件/脚本了。如执行`\cp -f /opt/service-render-output/zoo.cfg $ZOOKEEPER_HOME/conf/`。

```yaml
apiVersion: "apps/v1"
kind: "Deployment"
spec:
  template:
    containers:
      - name: "${roleServiceFullName}"
        command: [ "/bin/bash","-c" ]
        args:
          - |
            /bin/bash /opt/global/bootstrap.sh && \
            /bin/bash /opt/service-common/bootstrap.sh;
        env:
          - name: RENDER_TPL_DIR
            value: "/opt/service-render"
          - name: RENDER_MODEL
            value: "/opt/service-common/values.json"
        volumeMounts:
          - name: global-service-common
            mountPath: /opt/global/bootstrap.sh
            subPath: bootstrap.sh
          - name: global-render-config
            mountPath: /opt/global/10.render
          - name: service-render
            mountPath: /opt/service-render
          - name: service-common
            mountPath: /opt/service-common
    volumes:
      - name: global-service-common
        configMap:
          name: global-service-common
      - name: global-render-config
        configMap:
          name: global-render-config
      - name: service-render
        configMap:
          name: zookeeper-service-render
      - name: service-common
        configMap:
          name: zookeeper-service-common

```

## 全局启动脚本

全局启动脚本用于插件化统一化地完成容器的操作。如容器启动后的模板渲染操作、linux用户从configmmap中定时同步操作。

工作原理：
容器启动时先执行统一的/opt/global/bootstrap.sh脚本。该脚本会自动加载执行 /opt/global/任意文件夹 下的bootstrap.sh脚本。

所以只要选择性地将一些configmap放到 /opt/global 目录下，就可以像插件一样运行了。

参考如下，global-service-common用于挂载/opt/global/bootstrap.sh。其他则是3个插件。

```yaml
      containers:
        - image: "${conf['serverImage']}"
          command: [ "/bin/bash","-c" ]
          args:
            - |
              /bin/bash /opt/global/bootstrap.sh && \
              /bin/bash /opt/service-common/bootstrap.sh;
          volumeMounts:
            - name: global-service-common
              mountPath: /opt/global/bootstrap.sh
              subPath: bootstrap.sh
            - name: global-render-config
              mountPath: /opt/global/10.render
            - name: global-usersync-config
              mountPath: /opt/global/20.usersync
            - name: global-copy-filebeat-config
              mountPath: /opt/global/30.copy-filebeat-config
          volumes:
            - name: global-service-common
              configMap:
                name: global-service-common
            - name: global-render-config
              configMap:
                name: global-render-config
            - name: global-usersync-config
              configMap:
                name: global-usersync-config
            - name: global-copy-filebeat-config
              configMap:
                name: global-copy-filebeat-config

```

## service-info

这是一个简化后的组件信息文件

```yaml
# 名称
name: HDFS
# 用于显示的名称
label: "HDFS"
# 描述信息
description: "分布式大数据存储"
# 版本号
version: 3.3.4
# 依赖的组件名称列表
dependencies:
  - "ZOOKEEPER"
# 是否支持kerberos，目前该配置项无实际作用
supportKerberos: false

# grafana面板的uid，需与实际要注入的grafana面板uid匹配，可设置为""关闭监控面板
dashboard:
  uid: hdfs001

# 组件角色列表
roles:
  # 角色名称
  - name: HDFS_ZKFC_FORMAT
    # 角色用于显示的名称
    label: "ZKFC FORMAT"
    # 角色全名，用于注入k8s作为标识
    roleFullName: "hadoop-hdfs-zkfc-format"
    # 角色排序，会影响安装/卸载顺序
    sortNum: 0
    # 角色类型，目前支持JOB、HELM_CHART、DEPLOYMENT、EMPTY（不会试图生成对应k8s资源）
    type: JOB
    # 是否需要奇数节点数
    needOdd: true
    # 固定节点数，fixedNum和minNum设置一个就行了
    fixedNum: 1
    # 最小节点数
    #minNum: 3
  - name: HDFS_JOURNALNODE
    label: "Journal Node"
    roleFullName: "hadoop-hdfs-journalnode"
    # 访问链接，localhostname会被替换成真实的hostname或ip
    linkExpression: "http://${localhostname}:${conf['journalnode.http-port']}/?user.name=hdfs"
    sortNum: 1
    type: DEPLOYMENT

    needOdd: true
    minNum: 3
# 自定义配置文件
# 用户部署的时候可以给这些文件添加key-value，组件开发者需将这些key-value添加到对应文件里
customConfigFiles:
  - core-site.xml
  - hdfs-site.xml
  - httpfs-site.xml

# 自定义配置项，是模型变量的主要来源
configurations:
  # 参数名
  - name: serverImage
    # 描述
    description: "服务镜像"
    # 默认值
    recommendExpression: "registry.cn-guangzhou.aliyuncs.com/bigdata200/hadoop:3.3.4"
    # 参数值的类型，支持 InputNumber（数字）、InputString（字符串）、Switch（bool）、Select（枚举）、Slider（滑块）
    valueType: InputString
    # 是否在安装组件时出现该配置，如果为否则需要安装时看不到该配置，安装后才能在配置项界面看到
    # 当配置项较重要时应设置为true
    # 所有资源相关、端口相关的都应该设置为true
    configurableInWizard: true
    # 分类标签
    tag: "镜像"

  - name: dfs.client.socket-timeout
    recommendExpression: 120000
    # 参数值的单位，只影响前端显示，实际后端注入时用的只有value，没有unit
    unit: MILLISECONDS
    valueType: InputNumber
    # 所属的配置文件，需要在前面的customConfigFiles中添加
    confFile: "hdfs-site.xml"
    tag: "高级参数"

  - name: journalnode.use.wildcard
    recommendExpression: true
    valueType: Switch
    configurableInWizard: true
    tag: "常用参数"

  - name: global.imagePullPolicy
    description: "镜像拉取策略"
    recommendExpression: "IfNotPresent"
    valueType: Select
    # 当类型为Select，要提供可选项
    options: [ "IfNotPresent","Always","Never" ]
    configurableInWizard: true
    tag: "通用"
  - name: mapred.child.heapsize
    recommendExpression: 2048
    # 当类型为Slider时，需要提供最大值和最小值
    min: 1024
    max: 65536
    valueType: Slider
    unit: MB
    confFile: "mapred-site.xml"
    configurableInWizard: true
    tag: "资源管理"
```

## alert-rule.yaml

初始告警规则列表，可以在cloudeon界面中管理修改

```yaml
rules:
  # 规则名称
  - alert: Zookeeper进程存活
    # 触发告警的Prometheus表达式，支持#{{freemarker表达式}}#语法，见下
    promql: sum(up{job="metrics-zookeeper"})<#{{${serviceRoles['ZOOKEEPER_SERVER']?size}}}#
    # 告警级别，当前没有明确作用
    alertLevel: exception
    # 告警规则所属的组件角色名
    serviceRoleName: ZOOKEEPER_SERVER
    # 告警建议
    alertAdvice: Zookeeper发生宕机，请检查日志或执行重启
    # 告警信息，支持alertmanager的变量取值写法
    alertInfo: "ZookeeperServer发生宕机，当前存活实例数为：{{$value}}"


```

告警表达式promql是核心，为了增强其能力，这里添加#{{freemarker表达式}}#语法，用于注入cloudeon的模型变量。该功能依赖alert-rule.yaml.ftl中的executeDynamicCode方法。
使用如下：
"sum(up{job=\"metrics-zookeeper\"})<#{{${serviceRoles['ZOOKEEPER_SERVER']?size}}}#"
当zookeeper存活进程数低于目标数时告警。目标数根据实际配置注入。


