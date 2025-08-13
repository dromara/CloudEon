FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:latest

ENV KAFKA_HOME=/opt/kafka \
    KAFKA_VERSION=2.8.2
ENV PATH=${PATH}:${KAFKA_HOME}/bin

RUN wget https://mirrors.huaweicloud.com/apache/kafka/${KAFKA_VERSION}/kafka_2.12-${KAFKA_VERSION}.tgz \
    && tar -zxvf kafka*.tgz -C /opt \
    && mv /opt/kafka* $KAFKA_HOME \
    && rm -f kafka*.tgz

WORKDIR $KAFKA_HOME
