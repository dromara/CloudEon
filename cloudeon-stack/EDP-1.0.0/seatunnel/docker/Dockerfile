FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER hadoop

ENV  version="2.3.1"

RUN cd /home/hadoop/ && wget "https://archive.apache.org/dist/incubator/seatunnel/${version}/apache-seatunnel-incubating-${version}-bin.tar.gz"   \
   && tar -xzvf "apache-seatunnel-incubating-${version}-bin.tar.gz" -C /home/hadoop/ \
    && rm -f apache-seatunnel-incubating-${version}-bin.tar.gz && mv apache-seatunnel-incubating-${version}  apache-seatunnel

WORKDIR /home/hadoop/apache-seatunnel

ENV SEATUNNEL_HOME=/home/hadoop/apache-seatunnel

RUN $SEATUNNEL_HOME/bin/install-plugin.sh $version && rm -rf ~/.m2


ENV PATH=$PATH:$SEATUNNEL_HOME/bin



