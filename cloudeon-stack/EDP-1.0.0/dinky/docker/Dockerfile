FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

USER root

WORKDIR /opt

ARG FLINK_VERSION=1.16.1
ARG FLINK_BIG_VERSION=1.16
ARG SCALA_VERSION=2.12
ARG DINKY_VERSION=0.7.3

ADD dlink-release-${DINKY_VERSION}.tar.gz /opt/
ADD flink-${FLINK_VERSION}-bin-scala_${SCALA_VERSION}.tgz /opt/

ENV DINKY_HOME=/opt/dlink-release-${DINKY_VERSION}
ENV FLINK_HOME=/opt/flink-${FLINK_VERSION}

RUN yum install -y sudo && yum clean all && rm -rf /var/lib/yum/history/*.sqlite  && rm -rf /var/cache/yum

