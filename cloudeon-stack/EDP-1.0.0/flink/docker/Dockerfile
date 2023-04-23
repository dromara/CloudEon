FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER flink

WORKDIR /home/flink/

RUN wget https://archive.apache.org/dist/flink/flink-1.15.4/flink-1.15.4-bin-scala_2.12.tgz && tar -zxvf flink-1.15.4-bin-scala_2.12.tgz -C /home/flink/ \
    && rm -f flink-1.15.4-bin-scala_2.12.tgz && mv flink-1.15.4  apache-flink

ENV FLINK_HOME=/home/flink/apache-flink

ENV PATH=$PATH:$FLINK_HOME/bin



