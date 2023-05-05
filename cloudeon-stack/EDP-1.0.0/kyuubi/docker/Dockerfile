FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER kyuubi

WORKDIR /home/kyuubi/

ENV KYUUBI_HOME=/home/kyuubi/apache-kyuubi
ENV SPARK_HOME=/home/kyuubi/apache-spark

RUN wget https://archive.apache.org/dist/spark/spark-3.2.3/spark-3.2.3-bin-hadoop3.2.tgz && tar -zxvf spark-3.2.3-bin-hadoop3.2.tgz -C /home/kyuubi/ \
    && rm -f spark-3.2.3-bin-hadoop3.2.tgz && mv spark-3.2.3-bin-hadoop3.2  apache-spark



RUN wget https://archive.apache.org/dist/kyuubi/kyuubi-1.7.0/apache-kyuubi-1.7.0-bin.tgz && tar -zxvf apache-kyuubi-1.7.0-bin.tgz -C /home/kyuubi/ \
    && rm -f apache-kyuubi-1.7.0-bin.tgz && mv apache-kyuubi-1.7.0-bin  apache-kyuubi

ENV PATH=$PATH:$KYUUBI_HOME/bin:$SPARK_HOME/bin






