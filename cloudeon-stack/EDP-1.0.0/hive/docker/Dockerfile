FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER hive

WORKDIR /home/hive/

RUN wget https://archive.apache.org/dist/hadoop/common/hadoop-3.3.4/hadoop-3.3.4.tar.gz && tar -zxvf hadoop-3.3.4.tar.gz -C /home/hive/ \
    && rm -f hadoop-3.3.4.tar.gz && mv hadoop-3.3.4  apache-hadoop \
    && wget https://archive.apache.org/dist/hive/hive-3.1.3/apache-hive-3.1.3-bin.tar.gz && tar -zxvf apache-hive-3.1.3-bin.tar.gz -C /home/hive/ \
    && rm -f apache-hive-3.1.3-bin.tar.gz && mv apache-hive-3.1.3-bin  apache-hive

RUN wget https://repo1.maven.org/maven2/org/apache/hudi/hudi-hadoop-mr-bundle/0.13.1/hudi-hadoop-mr-bundle-0.13.1.jar
RUN wget https://repo1.maven.org/maven2/org/apache/hudi/hudi-hive-sync/0.13.1/hudi-hive-sync-0.13.1.jar
RUN mv *.jar /home/hive/apache-hive/lib/

ENV HADOOP_HOME=/home/hive/apache-hadoop
ENV HIVE_HOME=/home/hive/apache-hive

ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

ENV MYSQL_CONN_VERSION=8.0.20
ENV MYSQL_CONN_URL=https://downloads.mysql.com/archives/get/p/3/file/mysql-connector-java-${MYSQL_CONN_VERSION}.tar.gz
RUN  curl -fSL $MYSQL_CONN_URL -o /home/hive/mysql-connector-java.tar.gz     && tar -xzvf /home/hive/mysql-connector-java.tar.gz -C /home/hive/   \
  && cp /home/hive/mysql-connector-java-${MYSQL_CONN_VERSION}/mysql-connector-java-${MYSQL_CONN_VERSION}.jar $HIVE_HOME/lib/mysql-connector-java.jar  \
  && rm /home/hive/mysql-connector-java.tar.gz     && rm -rf /home/hive/mysql-connector-java-${MYSQL_CONN_VERSION}
