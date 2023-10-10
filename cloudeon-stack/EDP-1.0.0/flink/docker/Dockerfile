FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER flink

WORKDIR /home/flink/

RUN wget https://archive.apache.org/dist/flink/flink-1.15.4/flink-1.15.4-bin-scala_2.12.tgz && tar -zxvf flink-1.15.4-bin-scala_2.12.tgz -C /home/flink/ \
    && rm -f flink-1.15.4-bin-scala_2.12.tgz && mv flink-1.15.4  apache-flink  \
  &&   wget https://archive.apache.org/dist/hadoop/common/hadoop-3.3.4/hadoop-3.3.4.tar.gz && tar -zxvf hadoop-3.3.4.tar.gz -C /home/flink/ \
        && rm -f hadoop-3.3.4.tar.gz && mv hadoop-3.3.4  apache-hadoop

ENV FLINK_HOME=/home/flink/apache-flink
ENV HADOOP_HOME=/home/flink/apache-hadoop

ENV PATH=$PATH:$FLINK_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# iceberg support
RUN wget https://repo1.maven.org/maven2/org/apache/iceberg/iceberg-flink-runtime-1.15/1.2.1/iceberg-flink-runtime-1.15-1.2.1.jar