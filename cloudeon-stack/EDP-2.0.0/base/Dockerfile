FROM hub.oepkgs.net/openeuler/openeuler:24.03

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone

ENV TZ=Asia/Shanghai  \
    JAVA_HOME=/usr/lib/jvm/java-1.8.0 \
    FREEMARKER_GENERATOR_CLI_HOME=/opt/freemarker-generator-cli
ENV PATH=$PATH:$JAVA_HOME/bin:$FREEMARKER_GENERATOR_CLI_HOME/bin

RUN  yum  -y install  wget net-tools vim perl which  nc zip unzip lsof bc hostname crontabs java-1.8.0-openjdk-devel

ADD freemarker-generator-cli-0.2.0-SNAPSHOT-app.tar.gz /opt/

RUN mv /opt/freemarker-generator-cli-* /opt/freemarker-generator-cli
# setup jmx exporter
RUN wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar  \
    && mkdir -p /opt/jmx_exporter  \
    && mv  jmx_prometheus_javaagent-0.20.0.jar /opt/jmx_exporter/jmx_prometheus_javaagent.jar
