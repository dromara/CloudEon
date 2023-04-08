FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER hbase

WORKDIR /home/hbase/

RUN wget https://archive.apache.org/dist/hbase/2.4.16/hbase-2.4.16-bin.tar.gz && tar -zxvf hbase-2.4.16-bin.tar.gz -C /home/hbase/ \
    && rm -f hbase-2.4.16-bin.tar.gz && mv hbase-2.4.16  apache-hbase


ENV HBASE_HOME=/home/hbase/apache-hbase/
ENV PATH=$PATH:$HBASE_HOME/bin







