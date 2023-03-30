FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER hive

WORKDIR /home/hive/

RUN wget https://archive.apache.org/dist/hadoop/common/hadoop-3.3.4/hadoop-3.3.4.tar.gz && tar -zxvf hadoop-3.3.4.tar.gz -C /home/hive/ \
    && rm -f hadoop-3.3.4.tar.gz && mv hadoop-3.3.4  apache-hadoop \
    && wget https://archive.apache.org/dist/hive/hive-3.1.3/apache-hive-3.1.3-bin.tar.gz && tar -zxvf apache-hive-3.1.3-bin.tar.gz -C /home/hive/ \
    && rm -f apache-hive-3.1.3-bin.tar.gz && mv apache-hive-3.1.3-bin  apache-hive

ENV HADOOP_HOME=/home/hive/apache-hadoop
ENV HIVE_HOME=/home/hive/apache-hive

ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin



