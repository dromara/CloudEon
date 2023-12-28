FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

USER root

WORKDIR /opt

ARG FLINK_VERSION=1.15.4
ARG FLINK_BIG_VERSION=1.15
ARG SCALA_VERSION=2.12
ARG DINKY_VERSION=0.7.5
ARG HADOOP_VERSION=3.3.4

RUN wget   https://github.com/DataLinkDC/dinky/releases/download/v${DINKY_VERSION}/dlink-release-${DINKY_VERSION}.tar.gz  &&  tar zxvf dlink-release-${DINKY_VERSION}.tar.gz -C /opt && rm -f dlink-release-${DINKY_VERSION}.tar.gz \
&& wget https://archive.apache.org/dist/flink/flink-${FLINK_VERSION}/flink-${FLINK_VERSION}-bin-scala_2.12.tgz &&  tar zxvf flink-${FLINK_VERSION}-bin-scala_2.12.tgz -C /opt &&  rm -f flink-${FLINK_VERSION}-bin-scala_2.12.tgz   \
&& wget https://archive.apache.org/dist/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz && tar -zxvf hadoop-${HADOOP_VERSION}.tar.gz -C /opt/ && rm -f hadoop-${HADOOP_VERSION}.tar.gz

ENV DINKY_HOME=/opt/dlink-release-${DINKY_VERSION}
ENV FLINK_HOME=/opt/flink-${FLINK_VERSION}
ENV HADOOP_HOME=/opt/hadoop-${HADOOP_VERSION}
ENV HADOOP_CONF_DIR=/opt/edp/dinky/conf
ENV PATH=$PATH:$FLINK_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin


RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-jdbc/1.15.4/flink-connector-jdbc-1.15.4.jar
RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-elasticsearch7/1.15.4/flink-sql-connector-elasticsearch7-1.15.4.jar
RUN wget https://repo1.maven.org/maven2/org/apache/doris/flink-doris-connector-1.15/1.4.0/flink-doris-connector-1.15-1.4.0.jar
RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka/1.15.4/flink-sql-connector-kafka-1.15.4.jar
RUN wget https://repo1.maven.org/maven2/com/ververica/flink-sql-connector-mysql-cdc/2.4.1/flink-sql-connector-mysql-cdc-2.4.1.jar
RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-hive-3.1.2_2.12/1.15.4/flink-sql-connector-hive-3.1.2_2.12-1.15.4.jar
RUN wget https://repo.maven.apache.org/maven2/org/apache/hudi/hudi-flink1.15-bundle/0.13.1/hudi-flink1.15-bundle-0.13.1.jar
RUN wget https://repo1.maven.org/maven2/org/apache/calcite/calcite-core/1.35.0/calcite-core-1.35.0.jar
RUN mv   *.jar ${FLINK_HOME}/lib/
RUN wget https://repository.cloudera.com/artifactory/cloudera-repos/org/apache/flink/flink-shaded-hadoop-3-uber/3.1.1.7.2.9.0-173-9.0/flink-shaded-hadoop-3-uber-3.1.1.7.2.9.0-173-9.0.jar
RUN mv   flink-shaded-hadoop-3-uber-3.1.1.7.2.9.0-173-9.0.jar $DINKY_HOME/plugins/flink$FLINK_BIG_VERSION/dinky/
RUN rm -f *.jar

RUN sed -i 's/-1.14/-1.15/g' ${DINKY_HOME}/auto.sh
RUN yum install -y sudo mysql && yum clean all && rm -rf /var/lib/yum/history/*.sqlite  && rm -rf /var/cache/yum