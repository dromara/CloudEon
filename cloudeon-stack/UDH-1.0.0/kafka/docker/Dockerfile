FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER hadoop

WORKDIR /home/hadoop/

RUN wget https://archive.apache.org/dist/kafka/2.8.2/kafka_2.12-2.8.2.tgz && tar -zxvf kafka_2.12-2.8.2.tgz -C /home/hadoop/ \
    && rm -f kafka_2.12-2.8.2.tgz && mv kafka_2.12-2.8.2  apache-kafka

ENV KAFKA_HOME=/home/hadoop/
ENV PATH=$PATH:$KAFKA_HOME/apache-kafka/bin:$KAFKA_HOME/apache-kafka/bin
