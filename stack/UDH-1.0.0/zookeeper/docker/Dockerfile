FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

RUN wget https://archive.apache.org/dist/zookeeper/zookeeper-3.5.9/apache-zookeeper-3.5.9-bin.tar.gz && tar -zxvf apache-zookeeper-3.5.9-bin.tar.gz -C /opt/ \
    && rm -f apache-zookeeper-3.5.9-bin.tar.gz 

WORKDIR /opt/apache-zookeeper-3.5.9-bin

ENV PATH=$PATH:/opt/apache-zookeeper-3.5.9-bin/bin

