FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/hadoop:3.3.4

ENV FLINK_HOME=/opt/flink
ENV PATH=$PATH:$FLINK_HOME/bin

WORKDIR /home/flink/

RUN wget https://archive.apache.org/dist/flink/flink-1.15.4/flink-1.15.4-bin-scala_2.12.tgz && tar -zxvf flink-1.15.4-bin-scala_2.12.tgz -C /opt \
    && rm -f flink-*.tgz && mv /opt/flink-*  ${FLINK_HOME}


WORKDIR $FLINK_HOME

