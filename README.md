<div align="center">
<h1>CloudEon云原生大数据平台</h1>

[![GitHub Pull Requests](https://img.shields.io/github/stars/dromara/CloudEon)](https://github.com/dromara/CloudEon/stargazers)
[![Gitee Star](https://gitee.com/dromara/cloudeon/badge/star.svg?theme=gvp)](https://gitee.com/dromara/CloudEon/stargazers)



[![HitCount](https://views.whatilearened.today/views/github/dromara/CloudEon.svg)](https://github.com/dromara/CloudEon)
[![Commits](https://img.shields.io/github/commit-activity/m/dromara/CloudEon?color=ffff00)](https://github.com/dromara/CloudEon/commits/main)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit)](https://github.com/pre-commit/pre-commit)
[![All Contributors](https://img.shields.io/github/all-contributors/dromara/CloudEon)](#contributors-)
[![GitHub license](https://img.shields.io/github/license/dromara/CloudEon)](https://github.com/dromara/CloudEon/LICENSE)

<p> 🌉 构建于kubernetes集群之上的大数据管理平台 🌉</p>

<img src="https://camo.githubusercontent.com/82291b0fe831bfc6781e07fc5090cbd0a8b912bb8b8d4fec0696c881834f81ac/68747470733a2f2f70726f626f742e6d656469612f394575424971676170492e676966" width="800"  height="3">
</div><br>



## ℹ️ 项目简介

CloudEon是一款基于kubernetes的开源大数据平台，旨在为用户提供一种简单、高效、可扩展的大数据解决方案。该平台致力于简化多种大数据服务在kubernetes上的部署和管理，如Hadoop、Doris、Spark、Flink、Hive、Kafka等，能够满足不同规模和业务需求下的大数据处理和分析需求。

## 🔗 文档快链

项目相关介绍，使用，最佳实践等相关内容，都会在官方文档呈现，如有疑问，请先阅读官方文档，以下列举以下常用快链。

- [官网地址](https://cloudeon.top/)
- [项目介绍](https://docs.cloudeon.top/en/latest/)
- [安装部署](https://docs.cloudeon.top/en/latest/%E5%AE%89%E8%A3%85%E9%83%A8%E7%BD%B2/docker)
- [支持组件](https://docs.cloudeon.top/en/latest/%E6%94%AF%E6%8C%81%E7%BB%84%E4%BB%B6/supportservice/)
- [Roadmap](https://docs.cloudeon.top/en/latest/Roadmap/)


## 🔍 功能特点

- 🚀 **快速搭建大数据集群**：通过`CloudEon`，用户可以在kubernetes上快速搭建部署hadoop集群、doris集群等大数据集群，省去了手动安装和配置的繁琐过程。

- 🐳 **容器化运行所有大数据服务**：`CloudEon`将所有大数据服务都以容器方式运行，使得这些服务的部署和管理更加灵活和便捷，同时也能更好地利用kubernetes的资源调度和管理能力。

- 📈 **支持监控告警等功能**：`CloudEon`提供了监控告警等功能，帮助用户实时监控集群运行状态，及时发现和解决问题。

- 🔧 **支持配置修改等功能**：`CloudEon`还提供了配置修改等功能，使得用户能够更加灵活地管理和配置自己的大数据集群。

- 🤖 **自动化运维**：`CloudEon`通过自动化运维，能够降低集群管理的难度和人力成本，同时也能提高集群的可用性和稳定性。

- 👀 **可视化管理界面**：`CloudEon`提供了可视化的管理界面，使得用户能够更加直观地管理和监控自己的大数据集群

- 🔌 **灵活的扩展性**：提供了插件机制，让用户可以自定义拓展和安装更多的大数据服务。这个插件机制是基于开放API和标准化接口实现的，可以支持用户快速开发和集成新的服务。

- 📊 **多种大数据服务支持**：除了hadoop和doris，`CloudEon`还支持其他多种大数据服务的部署和管理，如Spark、Flink、Hive、Kyuubi等。

**页面功能概览：**

|           ![集群](https://user-images.githubusercontent.com/123344357/230782193-d2830fa7-92c8-4efc-a44e-df0e8742b012.png)           | ![告警](https://user-images.githubusercontent.com/123344357/230778648-653dc9a7-f78e-4f1d-9aaa-7689ad257f10.png)  |
|:---------------------------------------------------------------------------------------:|---------------------------------------------------------------------|
|  ![服务列表](https://user-images.githubusercontent.com/123344357/230782573-aca46f56-46d6-4eb1-b0e4-4dbaf78a9695.png)  |  ![新增服务](https://user-images.githubusercontent.com/123344357/230782108-b9e322b3-c1de-4ad1-a34d-d89ab0319252.png)  |
|           ![配置](https://user-images.githubusercontent.com/123344357/230782069-93574212-8628-4af5-934a-b09ea0c073e5.png)           | ![角色实例](https://user-images.githubusercontent.com/123344357/230778761-0accabf4-209e-4666-8b7d-0fe8dcd52056.png)  |
|           ![指令日志](https://user-images.githubusercontent.com/123344357/230778679-6520b845-e354-4a73-a661-fb5b7596f217.png)       | ![指令详情](https://user-images.githubusercontent.com/123344357/230778699-a152755b-8c66-40a8-8fdc-27aa1f8e239c.png) |



## 👨‍💻 开源地址

| 分类 |                        GitHub                        |                        Gitee                        |
| :--: | :--------------------------------------------------: | :-------------------------------------------------: |
| 后端 |  https://github.com/dromara/CloudEon   | https://gitee.com/dromara/CloudEon  |
| 前端 | https://github.com/dromara/CloudEon/tree/master/cloudeon-ui | https://gitee.com/dromara/CloudEon/tree/master/cloudeon-ui  |
| 大数据组件 | https://github.com/dromara/CloudEon/tree/master/cloudeon-stack | https://gitee.com/dromara/CloudEon/tree/master/cloudeon-stack  |


## 🏖开源初衷
容器化设计可以很好地屏蔽操作系统细节，提高隔离性，并在构建过程中提前安装依赖的外部环境和工具，从而使得后续的服务可以在不同的服务器环境中运行。

在研究Hadoop on Kubernetes时，我们采用了helm完成了服务依赖顺序、配置关联影响和容器编排等工作。我们还整合了Doris（多Fe多Be）、DolphinScheduler、Kyuubi和Spark等服务，并实现了使用一句helm install命令即可正确启动这些相互依赖的服务。

在使用helm + Kubernetes（具体可参考：[源码](https://github.com/Kyofin/hadoop-k8s) ）来部署管理大数据集群过程中，我们发现这种方法大大减少了部署时间，提高了部署大数据集群的成功率。然而，运维管理的体验不尽人意，因为使用通用型Kubernetes的管理工具来管理大数据服务是不容易的，这需要开发者掌握Pod、Service、Configmap等知识。

对于习惯使用类似Ambari、CM这样的平台的大数据工程师来说，学习成本还是太高了。因此，我们开发了CloudEon，并将其开源。

CloudEon致力于将大数据服务迁移到云上，并帮助大数据生态组件更好地与云原生相融合。

## 🥰 感谢

感谢如下优秀的项目，没有这些项目，不可能会有CloudEon：

- 后端技术栈
  - [springboot-v2.7.4](https://github.com/spring-projects/spring-boot)
  - [lombok-v1.18.12](https://github.com/projectlombok/lombok)
  - [hutool-v5.8.9](https://github.com/dromara/hutool)
  - [hibernate-v5.6.11](https://github.com/hibernate/hibernate-orm)
  - [freemarker-v2.3.31](https://github.com/apache/freemarker)
- 前端技术栈
  - [react](https://github.com/facebook/react)
  - [ant-design](https://github.com/ant-design/ant-design)

- 另外特别感谢
  - [Ambari](https://github.com/apache/ambari)  ：参考其对大数据组件的安全管理、可拓展大数据组件包管理
  - [datasophon](https://github.com/datasophon/datasophon) ：参考其优秀的监控告警体系


## 🤗 另外

- 如果觉得项目不错，麻烦动动小手点个⭐️star⭐️!
- 如果你还有其他想法或者需求，欢迎在issue中交流！


## 📝 使用登记

如果你所在公司使用了该项目，烦请在这里留下脚印，感谢支持🥳 [点我](https://github.com/dromara/CloudEon/issues/20)


## 💎 优秀软件推荐

- [🦊 Kyuubi：为数据湖查询引擎（例如Spark、Flink或Trino等）提供SQL服务](https://github.com/apache/kyuubi)
- [🦄 Doris：简单易用、高性能和统一的分析数据库](https://github.com/apache/doris/)
- [🐬 DolphinScheduler：分布式和可扩展的开源工作流调度平台](https://github.com/apache/dolphinscheduler)


## 🤝 贡献者

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Pandas886"><img src="https://avatars.githubusercontent.com/u/123344357?v=4?s=100" width="100px;" alt="Pandas886"/><br /><sub><b>Pandas886</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=Pandas886" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Mericol"><img src="https://avatars.githubusercontent.com/u/39690226?v=4?s=100" width="100px;" alt="Mericol"/><br /><sub><b>Mericol</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=Mericol" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Koyfin"><img src="https://avatars.githubusercontent.com/u/18548053?v=4?s=100" width="100px;" alt="Koyfin"/><br /><sub><b>Koyfin</b></sub></a><br /><a href="#infra-Koyfin" title="Infrastructure (Hosting, Build-Tools, etc)">🚇</a> <a href="https://github.com/dromara/CloudEon/commits?author=Koyfin" title="Tests">⚠️</a> <a href="https://github.com/dromara/CloudEon/commits?author=Koyfin" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/mechaelyoung"><img src="https://avatars.githubusercontent.com/u/44049993?v=4?s=100" width="100px;" alt="mechaelyoung"/><br /><sub><b>mechaelyoung</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=mechaelyoung" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://linshenkx.cn"><img src="https://avatars.githubusercontent.com/u/32978552?v=4?s=100" width="100px;" alt="且炼时光"/><br /><sub><b>且炼时光</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=linshenkx" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/tgluon"><img src="https://avatars.githubusercontent.com/u/26194129?v=4?s=100" width="100px;" alt="XiuhongTang"/><br /><sub><b>XiuhongTang</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=tgluon" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/KangTomwk"><img src="https://avatars.githubusercontent.com/u/25816207?v=4?s=100" width="100px;" alt="KangTomwk"/><br /><sub><b>KangTomwk</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=KangTomwk" title="Code">💻</a></td>
    </tr>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/pan3793"><img src="https://avatars.githubusercontent.com/u/26535726?v=4?s=100" width="100px;" alt="Cheng Pan"/><br /><sub><b>Cheng Pan</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=pan3793" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/limaiwang"><img src="https://avatars.githubusercontent.com/u/23052750?v=4?s=100" width="100px;" alt="maiwang li"/><br /><sub><b>maiwang li</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=limaiwang" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/liang281778527"><img src="https://avatars.githubusercontent.com/u/26902335?v=4?s=100" width="100px;" alt="liang281778527"/><br /><sub><b>liang281778527</b></sub></a><br /><a href="https://github.com/dromara/CloudEon/commits?author=liang281778527" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
