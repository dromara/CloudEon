FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/hadoop:3.3.4

ENV HIVE_HOME=/opt/hive
ENV PATH=$PATH:$HIVE_HOME/bin


RUN wget --no-check-certificate https://archive.apache.org/dist/hive/hive-3.1.3/apache-hive-3.1.3-bin.tar.gz && tar -zxvf apache-hive-3.1.3-bin.tar.gz -C /opt/ \
    && rm -f apache-hive-*-bin.tar.gz && mv /opt/apache-hive-*  $HIVE_HOME


ENV MYSQL_CONN_VERSION=8.0.20
ENV MYSQL_CONN_URL=https://downloads.mysql.com/archives/get/p/3/file/mysql-connector-java-${MYSQL_CONN_VERSION}.tar.gz
RUN  curl -fSL $MYSQL_CONN_URL -o /tmp/mysql-connector-java.tar.gz     && tar -xzvf /tmp/mysql-connector-java.tar.gz -C /tmp/   \
  && cp /tmp/mysql-connector-java-${MYSQL_CONN_VERSION}/mysql-connector-java-${MYSQL_CONN_VERSION}.jar $HIVE_HOME/lib/mysql-connector-java.jar  \
  && rm -rf /tmp/mysql-connector-*

WORKDIR $HIVE_HOME
