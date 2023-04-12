FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER root

WORKDIR /home/root/

RUN wget https://archive.apache.org/dist/dolphinscheduler/3.0.5/apache-dolphinscheduler-3.0.5-bin.tar.gz && tar -zxvf apache-dolphinscheduler-3.0.5-bin.tar.gz -C /home/root/ \
    && rm -f apache-dolphinscheduler-3.0.5-bin.tar.gz && mv apache-dolphinscheduler-3.0.5-bin  apache-dolphinscheduler

ENV DOLPHINSCHEDULER_HOME=/home/root/apache-dolphinscheduler




