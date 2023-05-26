FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

USER root

WORKDIR /opt

ARG FLINK_VERSION=1.16.1
ARG FLINK_BIG_VERSION=1.16
ARG SCALA_VERSION=2.12
ARG DINKY_VERSION=0.7.3

RUN wget   https://github.com/DataLinkDC/dinky/releases/download/v${DINKY_VERSION}/dlink-release-${DINKY_VERSION}.tar.gz  &&  tar zxvf dlink-release-${DINKY_VERSION}.tar.gz -C /opt && rm -f dlink-release-${DINKY_VERSION}.tar.gz \
 && wget https://archive.apache.org/dist/flink/flink-${FLINK_VERSION}/flink-${FLINK_VERSION}-bin-scala_2.12.tgz &&  tar zxvf flink-${FLINK_VERSION}-bin-scala_2.12.tgz -C /opt &&  rm -f flink-${FLINK_VERSION}-bin-scala_2.12.tgz
 
ENV DINKY_HOME=/opt/dlink-release-${DINKY_VERSION}
ENV FLINK_HOME=/opt/flink-${FLINK_VERSION}

RUN yum install -y sudo mysql && yum clean all && rm -rf /var/lib/yum/history/*.sqlite  && rm -rf /var/cache/yum
