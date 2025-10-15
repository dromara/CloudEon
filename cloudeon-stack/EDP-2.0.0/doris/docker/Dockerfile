FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:8-ubuntu


ENV DORIS_HOME=/opt/doris
ENV PATH=$PATH:$DORIS_HOME/fe/bin:$DORIS_HOME/be/bin

WORKDIR /tmp

ARG DORIS_DOWNLOAD_URL=https://apache-doris-releases.oss-accelerate.aliyuncs.com/apache-doris-2.1.11-bin-x64.tar.gz

RUN wget $DORIS_DOWNLOAD_URL \
    && tar -xvf apache-doris-*.tar.gz -C /opt \
    && rm -f apache-doris-*.tar.gz && mv /opt/apache-doris-*  $DORIS_HOME \
    && apt-get update \
    && apt-get install -y mariadb-client

WORKDIR $DORIS_HOME
