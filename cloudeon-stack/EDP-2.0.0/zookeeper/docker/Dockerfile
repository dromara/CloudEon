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

