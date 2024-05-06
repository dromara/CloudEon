FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/spark:3.2.3

ENV KYUUBI_HOME=/opt/kyuubi
ENV PATH=$PATH:$KYUUBI_HOME/bin

WORKDIR /tmp

RUN wget https://archive.apache.org/dist/kyuubi/kyuubi-1.7.0/apache-kyuubi-1.7.0-bin.tgz && tar -zxvf apache-kyuubi-*.tgz -C /opt \
    && rm -f apache-kyuubi-*.tgz && mv /opt/apache-kyuubi-*  ${KYUUBI_HOME}

WORKDIR $KYUUBI_HOME