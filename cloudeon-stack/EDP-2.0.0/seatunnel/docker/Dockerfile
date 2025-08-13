FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:latest

ENV SEATUNNEL_VERSION="2.3.7"
ENV SEATUNNEL_HOME="/opt/seatunnel"
ENV PATH=$PATH:$SEATUNNEL_HOME/bin

RUN wget https://dlcdn.apache.org/seatunnel/${SEATUNNEL_VERSION}/apache-seatunnel-${SEATUNNEL_VERSION}-bin.tar.gz
RUN tar -xzvf apache-seatunnel-${SEATUNNEL_VERSION}-bin.tar.gz
RUN mv apache-seatunnel-${SEATUNNEL_VERSION} ${SEATUNNEL_HOME}

WORKDIR $SEATUNNEL_HOME

RUN install-plugin.sh ${SEATUNNEL_VERSION}






