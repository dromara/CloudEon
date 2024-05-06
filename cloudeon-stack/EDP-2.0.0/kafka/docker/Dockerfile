FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:latest

ENV KAFKA_HOME=/opt/kafka
ENV PATH=${PATH}:${KAFKA_HOME}/bin

RUN wget https://archive.apache.org/dist/kafka/2.8.2/kafka_2.12-2.8.2.tgz \
    && tar -zxvf kafka_2.12-2.8.2.tgz -C /opt \
    && mv /opt/kafka* $KAFKA_HOME \
    && rm -f kafka*.tgz

WORKDIR $KAFKA_HOME
