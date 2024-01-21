FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:latest

ENV ELASTICSEARCH_HOME=/opt/elasticsearch \
    ELASTICSEARCH_VERSION=7.16.3
ENV PATH=${PATH}:${ELASTICSEARCH_HOME}/bin \
    ES_JAVA_HOME=$ELASTICSEARCH_HOME/jdk \
    ES_HOME=$ELASTICSEARCH_HOME

RUN groupadd elasticsearch \
    && useradd -g elasticsearch  elasticsearch \
    && chmod 777 /opt

USER elasticsearch
WORKDIR /opt

RUN wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-${ELASTICSEARCH_VERSION}-linux-x86_64.tar.gz \
    && tar -zxvf elasticsearch-*-linux-x86_64.tar.gz -C /opt \
    && rm -f elasticsearch-*.tar.gz \
    && mv /opt/elasticsearch* $ELASTICSEARCH_HOME

USER root

WORKDIR $ELASTICSEARCH_HOME
