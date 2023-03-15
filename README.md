<div align="center">
<h1>CloudEon</h1>

[![GitHub Pull Requests](https://img.shields.io/github/stars/eryajf/go-ldap-admin)](https://github.com/eryajf/go-ldap-admin/stargazers)
[![HitCount](https://views.whatilearened.today/views/github/eryajf/go-ldap-admin.svg)](https://github.com/eryajf/go-ldap-admin)
[![GitHub license](https://img.shields.io/github/license/eryajf/go-ldap-admin)](https://github.com/eryajf/go-ldap-admin/blob/main/LICENSE)
[![Commits](https://img.shields.io/github/commit-activity/m/eryajf/go-ldap-admin?color=ffff00)](https://github.com/eryajf/go-ldap-admin/commits/main)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit)](https://github.com/pre-commit/pre-commit)
[![All Contributors](https://img.shields.io/badge/all_contributors-2-orange.svg?style=flat-square)](#contributors-)

<p> 🌉 构建于K8s集群之上的大数据集群管理平台 🌉</p>

<img src="https://camo.githubusercontent.com/82291b0fe831bfc6781e07fc5090cbd0a8b912bb8b8d4fec0696c881834f81ac/68747470733a2f2f70726f626f742e6d656469612f394575424971676170492e676966" width="800"  height="3">
</div><br>


<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [ℹ️ 项目简介](#-%E9%A1%B9%E7%9B%AE%E7%AE%80%E4%BB%8B)
- [🏊 在线体验](#-%E5%9C%A8%E7%BA%BF%E4%BD%93%E9%AA%8C)
- [👨‍💻 项目地址](#-%E9%A1%B9%E7%9B%AE%E5%9C%B0%E5%9D%80)
- [🔗 文档快链](#-%E6%96%87%E6%A1%A3%E5%BF%AB%E9%93%BE)
- [🥰 感谢](#-%E6%84%9F%E8%B0%A2)
- [🤗 另外](#-%E5%8F%A6%E5%A4%96)
- [🤑 捐赠](#-%E6%8D%90%E8%B5%A0)
- [📝 使用登记](#-%E4%BD%BF%E7%94%A8%E7%99%BB%E8%AE%B0)
- [💎 优秀软件推荐](#-%E4%BC%98%E7%A7%80%E8%BD%AF%E4%BB%B6%E6%8E%A8%E8%8D%90)
- [🤝 贡献者](#-%E8%B4%A1%E7%8C%AE%E8%80%85)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## ℹ️ 项目简介

`go-ldap-admin`旨在为`OpenLDAP`服务端提供一个简单易用，清晰美观的现代化管理后台。

> 在完成针对`OpenLDAP`的管理能力之下，支持对`钉钉`，`企业微信`，`飞书`的集成，用户可以选择手动或者自动同步组织架构以及员工信息到平台中，让`go-ldap-admin`项目成为打通企业IM与企业内网应用之间的桥梁。

## 🏊 在线体验

提供在线体验地址如下：

|     分类      |                             地址                             |          用户名           | 密码   |
| :-----------: | :----------------------------------------------------------: | :-----------------------: | ------ |
| go-ldap-admin | [http://demo-go-ldap-admin.eryajf.net](http://demo-go-ldap-admin.eryajf.net) |           admin           | 123456 |
| phpLdapAdmin  | [http://demo-go-ldap-admin.eryajf.net:8091/](http://demo-go-ldap-admin.eryajf.net:8091/) | cn=admin,dc=eryajf,dc=net | 123456 |

在线环境可能不稳，如果遇到访问异常，或者数据错乱，请联系我进行修复。

**页面功能概览：**

| ![登录页](https://cdn.staticaly.com/gh/eryajf/tu/main/img/image_20220724_165411.png) | ![首页](https://cdn.staticaly.com/gh/eryajf/tu/main/img/image_20220724_165545.png) |
| :----------------------------------------------------------: | ------------------------------------------------------------ |
| ![用户管理](https://cdn.staticaly.com/gh/eryajf/tu/main/img/image_20220724_165623.png) | ![分组管理](https://cdn.staticaly.com/gh/eryajf/tu/main/img/image_20220724_165701.png) |
| ![字段关系管理](https://cdn.staticaly.com/gh/eryajf/tu/main/img/image_20220724_165853.png) | ![菜单管理](https://cdn.staticaly.com/gh/eryajf/tu/main/img/image_20220724_165954.png) |
| ![接口管理](https://cdn.staticaly.com/gh/eryajf/tu/main/img/image_20220724_170015.png) | ![操作日志](https://cdn.staticaly.com/gh/eryajf/tu/main/img/image_20220724_170035.png) |

## 👨‍💻 项目地址

| 分类 |                        GitHub                        |                        Gitee                        |
| :--: | :--------------------------------------------------: | :-------------------------------------------------: |
| 后端 |  https://github.com/eryajf/go-ldap-admin.git   |  https://gitee.com/eryajf-world/go-ldap-admin.git   |
| 前端 | https://github.com/eryajf/go-ldap-admin-ui.git | https://gitee.com/eryajf-world/go-ldap-admin-ui.git |

## 🔗 文档快链

项目相关介绍，使用，最佳实践等相关内容，都会在官方文档呈现，如有疑问，请先阅读官方文档，以下列举以下常用快链。

- [官网地址](http://ldapdoc.eryajf.net)
- [项目背景](http://ldapdoc.eryajf.net/pages/101948/)
- [快速开始](http://ldapdoc.eryajf.net/pages/706e78/)
- [功能概览](http://ldapdoc.eryajf.net/pages/7a40de/)
- [本地开发](http://ldapdoc.eryajf.net/pages/cb7497/)
- [生产部署](http://ldapdoc.eryajf.net/pages/5769c4/)

## 🥰 感谢

感谢如下优秀的项目，没有这些项目，不可能会有go-ldap-admin：

- 后端技术栈
  - [Gin-v1.6.3](https://github.com/gin-gonic/gin)
  - [Gorm-v1.24.5](https://github.com/go-gorm/gorm)
  - [Sqlite-v1.7.0](https://github.com/glebarez/sqlite)
  - [Go-ldap-v3.4.2](https://github.com/go-ldap/ldap)
  - [Casbin-v2.22.0](https://github.com/casbin/casbin)
- 前端技术栈
  - [axios](https://github.com/axios/axios)
  - [element-ui](https://github.com/ElemeFE/element)

- 另外感谢
  - [go-web-mini](https://github.com/gnimli/go-web-mini)：项目基于该项目重构而成，感谢作者的付出。

## 🤗 另外

- 如果觉得项目不错，麻烦动动小手点个⭐️star⭐️!
- 如果你还有其他想法或者需求，欢迎在issue中交流！

## 🤑 捐赠

如果你觉得这个项目对你有帮助，你可以请作者喝杯咖啡 ☕️ [点我](http://ldapdoc.eryajf.net/pages/2b6725/)

## 📝 使用登记

如果你所在公司使用了该项目，烦请在这里留下脚印，感谢支持🥳 [点我](https://github.com/eryajf/go-ldap-admin/issues/18)

## 💎 优秀软件推荐

- [🦄 ConsulManager：高效易用的Consul Web运维平台](https://github.com/starsliao/ConsulManager)

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Pandas886"><img src="https://avatars.githubusercontent.com/u/123344357?v=4?s=100" width="100px;" alt="Pandas886"/><br /><sub><b>Pandas886</b></sub></a><br /><a href="https://github.com/Pandas886/e-mapreduce/commits?author=Pandas886" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/ZMeiMei"><img src="https://avatars.githubusercontent.com/u/39690226?v=4?s=100" width="100px;" alt="ZMeiMei"/><br /><sub><b>ZMeiMei</b></sub></a><br /><a href="https://github.com/Pandas886/e-mapreduce/commits?author=ZMeiMei" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
