# 开发步骤

## 1 编写info文件

这个过程确定组件的基础信息、角色信息
配置项、配置文件可以先不写

另外需要寻找logo文件存放到icons目录下

## 2 编写docker镜像构造/获取脚本

有个别镜像直接pull、tag、push即可，但大部分需要自己构建。
以下是相关的规范建议：

1. 使用base或其他组件镜像作为基础镜像
2. 在环境变量中设置组件目录，目录通常为 /opt/组件名
3. 在下一个环境变量中将 组件执行路径（通常是 组件目录/bin）添加到 PATH中
4. 添加组件所要求的环境变量，如 ZOOBINDIR、HADOOP_CONF_DIR 等
5. 通过网络（通常是wget）获取组件安装包并解压到前面设置的组件目录
6. 设置工作目录为组件目录

dockerfile的核心是配置基础环境变量和下载安装软件，其他和业务相关的操作应放到启动脚本里面去，保持dockerfile整洁

```dockerfile
FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:latest

ENV ZOOKEEPER_HOME=/opt/zookeeper \
    ZOOKEEPER_VERSION=3.7.1
ENV PATH=${PATH}:${ZOOKEEPER_HOME}/bin \
    ZOOBINDIR=${ZOOKEEPER_HOME}/bin

RUN wget https://archive.apache.org/dist/zookeeper/zookeeper-${ZOOKEEPER_VERSION}/apache-zookeeper-${ZOOKEEPER_VERSION}-bin.tar.gz \
    && tar -zxvf apache-zookeeper-*-bin.tar.gz -C /opt \
    && mv /opt/apache-zookeeper-* $ZOOKEEPER_HOME \
    && rm -f apache-zookeeper-*-bin.tar.gz

WORKDIR $ZOOKEEPER_HOME


```

## 3 根据角色信息在k8s文件夹下编写资源文件

这里的资源文件主要复制自其他组件进行修改即可，同类型组件结构都差不多的。
核心的差异是在启动脚本里面。

这里需补充说明：

1. 参考全局启动脚本部分的说明编写command、args、volumeMounts、volumes
2. 环境变量至少需要额外添加NODE_NAME、MEM_LIMIT，让启动脚本可以获取到期节点名称、内存限制信息
3. 存储挂载至少需要额外添加 /etc/localtime，用于同步时区
4. 数据存储挂载及多盘挂载见下

## 4 根据角色的实际情况编写配置文件

这部分对应service-common和service-render目录

其中service-common通常写自定义功能脚本，如启动、检活脚本
service-render写需要注入变量渲染的组件本身的配置文件

如果配置文件本身和组件自带的完全一样，并不需要修改/渲染，则应该直接不写，用组件默认的就行

## 5 完善监控告警

### 1 指标采集

首先需要在k8s-render添加一个metrics-service，以便由serviceMonitor自动发现抓取指标数据。
默认的ServiceMonitor（default-service-monitor）会根据标签enable-default-service-monitor="true"
抓取service的metrics端口。如果有特殊要求可以自己写个ServiceMonitor放到kube-prometheus-render目录下。

### 2 监控面板

grafana面板对应的k8s资源为Configmap类型。其label需要包含grafana_dashboard="1"，其annotations需要包含folder="
${serviceFullName}"。然后在configmap里面放个k8s-dashboard.json就行了。其uid需要与info文件的一致，个人习惯统一命名为 组件名001
，另外其templating的Datasource设置隐藏，通常只保留Namespace和Instance两个查询项。面板的原始来源主要有：官方exporter的面板，grafana云服务提供的各组件的面板。这两个都没有再尝试寻找第三方面板。

### 3 告警规则

告警规则的模板文件是kube-prometheus-render/alert-rule.yaml.ftl，无特殊情况每个组件的这个文件都一样。关键的规则实体在alert-rule.yaml.ftl中编写。但是需要注意，alert-rule.yaml.ftl中的规则只是默认规则，只在平台初次加载时生效，后面规则内容在平台中进行管理，如增删改，并不会再反映到这个文件上。
