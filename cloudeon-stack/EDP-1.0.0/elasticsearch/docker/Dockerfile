FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

USER elasticsearch

WORKDIR /home/elasticsearch/

RUN wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.16.3-linux-x86_64.tar.gz && tar -zxvf elasticsearch-7.16.3-linux-x86_64.tar.gz -C /home/elasticsearch/ \
    && rm -f elasticsearch-7.16.3-linux-x86_64.tar.gz && mv elasticsearch-7.16.3 elasticsearch && wget https://github.com/justwatchcom/elasticsearch_exporter/releases/download/v1.1.0/elasticsearch_exporter-1.5.0.linux-amd64.tar.gz \
    && tar -zxvf elasticsearch_exporter-1.5.0.linux-amd64.tar.gz -C /home/elasticsearch/ && rm -f elasticsearch_exporter-1.5.0.linux-amd64.tar.gz && mv  elasticsearch_exporter-1.1.0.linux-amd64  elasticsearch_exporter

ENV ELASTICSEARCH_HOME=/home/elasticsearch/
ENV PATH=$PATH:$ELASTICSEARCH_HOME/elasticsearch/bin