FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/flink:1.15.4

ENV AMORO_HOME=/opt/amoro
ENV PATH=$PATH:$AMORO_HOME/bin

RUN yum install -y unzip

RUN wget https://github.com/apache/amoro/releases/download/v0.6.1/amoro-flink-runtime-1.15-0.6.1.jar \
    && mv amoro-flink-runtime-1.15-0.6.1.jar ${FLINK_HOME}/lib/

RUN wget https://github.com/apache/amoro/releases/download/v0.6.1/amoro-0.6.1-bin.zip

RUN unzip  amoro-*-bin.zip -d /opt/ \
    && rm -f amoro-*-bin.zip && mv /opt/amoro-*  $AMORO_HOME

RUN yum install -y mariadb.x86_64 mariadb-libs.x86_64

ENV MYSQL_JDBC_DRIVER_VERSION=8.0.30
RUN wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/${MYSQL_JDBC_DRIVER_VERSION}/mysql-connector-java-${MYSQL_JDBC_DRIVER_VERSION}.jar \
    && mv mysql-connector-java-${MYSQL_JDBC_DRIVER_VERSION}.jar $AMORO_HOME/lib/

WORKDIR $AMORO_HOME