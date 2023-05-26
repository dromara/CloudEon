FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER hadoop

ENV HADOOP_HOME=/home/hadoop/apache-hadoop
ENV KYLIN_HOME=/home/hadoop/apache-kylin
ENV HIVE_HOME=/home/hadoop/apache-hive
WORKDIR /home/hadoop/

RUN wget --no-check-certificate  https://archive.apache.org/dist/kylin/apache-kylin-5.0.0-alpha/apache-kylin-5.0.0-alpha-bin.tar.gz && tar -zxvf apache-kylin-5.0.0-alpha-bin.tar.gz -C /home/hadoop/ \
    && rm -f apache-kylin-5.0.0-alpha-bin.tar.gz && mv apache-kylin-5.0.0-alpha-bin  apache-kylin \
 &&   wget --no-check-certificate https://archive.apache.org/dist/hadoop/common/hadoop-3.3.4/hadoop-3.3.4.tar.gz && tar -zxvf hadoop-3.3.4.tar.gz -C /home/hadoop/ \
        && rm -f hadoop-3.3.4.tar.gz && mv hadoop-3.3.4  apache-hadoop \
     &&   wget https://archive.apache.org/dist/hive/hive-3.1.3/apache-hive-3.1.3-bin.tar.gz && tar -zxvf apache-hive-3.1.3-bin.tar.gz -C /home/hadoop/ \
       && rm -f apache-hive-3.1.3-bin.tar.gz && mv apache-hive-3.1.3-bin  apache-hive


ENV MYSQL_CONN_VERSION=8.0.20
ENV MYSQL_CONN_URL=https://downloads.mysql.com/archives/get/p/3/file/mysql-connector-java-${MYSQL_CONN_VERSION}.tar.gz
RUN curl -fSL $MYSQL_CONN_URL -o /home/hadoop/mysql-connector-java.tar.gz   \
  && tar -xzvf /home/hadoop/mysql-connector-java.tar.gz -C /home/hadoop/   \
    && cp /home/hadoop/mysql-connector-java-${MYSQL_CONN_VERSION}/mysql-connector-java-${MYSQL_CONN_VERSION}.jar $KYLIN_HOME/lib/ext/mysql-connector-java.jar  \
         && rm /home/hadoop/mysql-connector-java.tar.gz     && rm -rf /home/hadoop/mysql-connector-java-${MYSQL_CONN_VERSION}
WORKDIR /home/hadoop/apache-kylin
RUN /home/hadoop/apache-kylin/sbin/download-spark-user.sh && rm -rf spark-newten-3.2.0-4.6.3.0.tgz && rm -rf hive_1_2_2.tar.gz

ENV PATH=$PATH:$KYLIN_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

