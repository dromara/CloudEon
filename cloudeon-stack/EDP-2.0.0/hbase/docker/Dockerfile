FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/hadoop:3.3.4

ENV HBASE_HOME=/opt/hbase
ENV PATH=$PATH:$HBASE_HOME/bin

WORKDIR /tmp/

RUN wget https://archive.apache.org/dist/hbase/2.4.16/hbase-2.4.16-bin.tar.gz && tar -zxvf hbase-*-bin.tar.gz -C /opt \
    && rm -f hbase-*-bin.tar.gz && mv /opt/hbase-* $HBASE_HOME

WORKDIR $HBASE_HOME








