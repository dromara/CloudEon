FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER zookeeper

WORKDIR /home/zookeeper/

RUN wget https://archive.apache.org/dist/zookeeper/zookeeper-3.5.9/apache-zookeeper-3.5.9-bin.tar.gz && tar -zxvf apache-zookeeper-3.5.9-bin.tar.gz -C /home/zookeeper/ \
    && rm -f apache-zookeeper-3.5.9-bin.tar.gz 


ENV PATH=$PATH:/home/zookeeper/apache-zookeeper-3.5.9-bin/bin
ENV ZOOKEEPER_HOME=/home/zookeeper/apache-zookeeper-3.5.9-bin



COPY gcutil.sh /home/zookeeper
COPY health_check.sh /home/zookeeper
COPY memory_dump.sh /home/zookeeper
COPY show_zookeeper_role.sh /home/zookeeper
COPY bootstrap.sh /home/zookeeper


CMD /home/zookeeper/bootstrap.sh