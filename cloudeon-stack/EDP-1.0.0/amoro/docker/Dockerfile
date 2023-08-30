FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

RUN yum install -y unzip

USER hadoop

WORKDIR /home/hadoop/

RUN  wget https://github.com/NetEase/amoro/releases/download/v0.5.0/amoro-0.5.0-bin.zip

RUN unzip  amoro-0.5.0-bin.zip \
    && rm -f amoro-0.5.0-bin.zip && mv amoro-0.5.0  amoro

ENV AMORO_HOME=/home/hadoop/amoro

ENV PATH=$PATH:$AMORO_HOME/bin

RUN wget https://archive.apache.org/dist/flink/flink-1.15.4/flink-1.15.4-bin-scala_2.12.tgz && tar -zxvf flink-1.15.4-bin-scala_2.12.tgz -C /home/hadoop/ \
    && rm -f flink-1.15.4-bin-scala_2.12.tgz && mv flink-1.15.4  apache-flink  \
  &&   wget https://archive.apache.org/dist/hadoop/common/hadoop-3.3.4/hadoop-3.3.4.tar.gz && tar -zxvf hadoop-3.3.4.tar.gz -C /home/hadoop/ \
        && rm -f hadoop-3.3.4.tar.gz && mv hadoop-3.3.4  apache-hadoop

ENV FLINK_HOME=/home/hadoop/apache-flink
ENV HADOOP_HOME=/home/hadoop/apache-hadoop

ENV PATH=$PATH:$FLINK_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin