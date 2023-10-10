FROM centos:7 as build

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

ENV TZ=Asia/Shanghai  \
    JAVA_HOME=/opt/jdk1.8.0 \
    FREEMARKER_GENERATOR_CLI_HOME=/opt/freemarker-generator-cli
ENV PATH=$PATH:$JAVA_HOME/bin:$FREEMARKER_GENERATOR_CLI_HOME/bin

RUN sed -e 's|^mirrorlist=|#mirrorlist=|g' \
             -e 's|^#baseurl=http://mirror.centos.org|baseurl=https://mirrors.tuna.tsinghua.edu.cn|g' \
             -i.bak \
             /etc/yum.repos.d/CentOS-*.repo

RUN  yum  -y install  wget net-tools vim perl which  nc zip unzip lsof bc hostname crontabs

COPY wgetLatestJdk.sh /tmp/
ADD freemarker-generator-cli-0.2.0-SNAPSHOT-app.tar.gz /opt/

RUN mv /opt/freemarker-generator-cli-* /opt/freemarker-generator-cli \
    && source /tmp/wgetLatestJdk.sh -P /opt \
    && tar -zxvf /opt/jdk-*.tar.gz -C /opt/ \
    && rm -f /opt/jdk-*.tar.gz \
    && mv /opt/jdk1.8.0_* $JAVA_HOME

# setup jmx exporter
RUN wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar  \
    && mkdir -p /opt/jmx_exporter  \
    && mv  jmx_prometheus_javaagent-0.20.0.jar /opt/jmx_exporter/jmx_prometheus_javaagent.jar
