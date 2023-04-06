<div align="center">
<h1>CloudEon云原生大数据平台</h1>

[![GitHub Pull Requests](https://img.shields.io/github/stars/Pandas886/e-mapreduce)](https://github.com/Pandas886/e-mapreduce/stargazers)
[![HitCount](https://views.whatilearened.today/views/github/Pandas886/e-mapreduce.svg)](https://github.com/Pandas886/e-mapreduce)
[![Commits](https://img.shields.io/github/commit-activity/m/Pandas886/e-mapreduce?color=ffff00)](https://github.com/Pandas886/e-mapreduce/commits/main)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit)](https://github.com/pre-commit/pre-commit)
[![All Contributors](https://img.shields.io/badge/all_contributors-3-orange.svg?style=flat-square)](#contributors-)
[![GitHub license](https://img.shields.io/github/license/Pandas886/e-mapreduce)](https://github.com/Pandas886/e-mapreduce/LICENSE)

<p> 🌉 构建于kubernetes集群之上的大数据集群管理平台 🌉</p>

<img src="https://camo.githubusercontent.com/82291b0fe831bfc6781e07fc5090cbd0a8b912bb8b8d4fec0696c881834f81ac/68747470733a2f2f70726f626f742e6d656469612f394575424971676170492e676966" width="800"  height="3">
</div><br>



## ℹ️ 项目简介

`CloudEon`是一款基于`kubernetes`的开源大数据平台，旨在为用户提供一种简单、高效、可扩展的大数据解决方案。该平台支持多种大数据服务的部署和管理，如hadoop、doris、Spark、Flink、Hive等，能够满足不同规模和业务需求下的大数据处理和分析需求。

## 🔗 文档快链

项目相关介绍，使用，最佳实践等相关内容，都会在官方文档呈现，如有疑问，请先阅读官方文档，以下列举以下常用快链。

- [官网地址](https://cloudeon.top//)
- [项目介绍](https://cloudeon.readthedocs.io/en/latest/)
- [安装部署](https://cloudeon.readthedocs.io/en/latest/instal/)
- [支持组件](https://cloudeon.readthedocs.io/en/latest/supportservice/)
- [Roadmap](https://cloudeon.readthedocs.io/en/latest/Roadmap/)


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

| ![登录页面](http://image-picgo.test.upcdn.net/img/20230322182700.png) | ![节点列表](http://image-picgo.test.upcdn.net/img/20230322182617.png)  |
|:---------------------------------------------------------------------:|---------------------------------------------------------------------|
|  ![服务详情](http://image-picgo.test.upcdn.net/img/20230322181657.png) |  ![服务角色](http://image-picgo.test.upcdn.net/img/20230322181531.png)  |
|   ![服务配置](http://image-picgo.test.upcdn.net/img/20230316100517.png)   | ![指令执行详情](http://image-picgo.test.upcdn.net/img/20230316100553.png) |
|   ![指令日志](http://image-picgo.test.upcdn.net/img/20230316100634.png)   | ![新增服务](http://image-picgo.test.upcdn.net/img/20230322181806.png)   |
|   ![分配角色](http://image-picgo.test.upcdn.net/img/20230316100747.png)   | ![自定义配置](http://image-picgo.test.upcdn.net/img/20230316100833.png)  |


## 👨‍💻 项目地址

| 分类 |                        GitHub                        |                        Gitee                        |
| :--: | :--------------------------------------------------: | :-------------------------------------------------: |
| 后端 |  https://github.com/Pandas886/CloudEon   | https://gitee.com/Pandas886/CloudEon  |
| 前端 | https://github.com/Pandas886/CloudEon/tree/master/cloudeon-ui | https://gitee.com/Pandas886/CloudEon/tree/master/cloudeon-ui  |



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

- 另外感谢
  - [datasophon](https://github.com/datasophon/datasophon)
  - [Ambari](https://github.com/apache/ambari)

## 🤗 另外

- 如果觉得项目不错，麻烦动动小手点个⭐️star⭐️!
- 如果你还有其他想法或者需求，欢迎在issue中交流！


## 📝 使用登记

如果你所在公司使用了该项目，烦请在这里留下脚印，感谢支持🥳 [点我](https://github.com/Pandas886/CloudEon/issues/8)


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
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Pandas886"><img src="https://avatars.githubusercontent.com/u/123344357?v=4?s=100" width="100px;" alt="Pandas886"/><br /><sub><b>Pandas886</b></sub></a><br /><a href="https://github.com/Pandas886/CloudEon/commits?author=Pandas886" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Mericol"><img src="https://avatars.githubusercontent.com/u/39690226?v=4?s=100" width="100px;" alt="Mericol"/><br /><sub><b>Mericol</b></sub></a><br /><a href="https://github.com/Pandas886/CloudEon/commits?author=Mericol" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/huzk8"><img src="https://avatars.githubusercontent.com/u/18548053?v=4?s=100" width="100px;" alt="huzk"/><br /><sub><b>Pine</b></sub></a><br /><a href="#infra-huzk8" title="Infrastructure (Hosting, Build-Tools, etc)">🚇</a> </td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/mechaelyoung"><img src="https://avatars.githubusercontent.com/u/44049993?v=4?s=100" width="100px;" alt="mechaelyoung"/><br /><sub><b>mechaelyoung</b></sub></a><br /><a href="https://github.com/Pandas886/CloudEon/commits?author=mechaelyoung" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
