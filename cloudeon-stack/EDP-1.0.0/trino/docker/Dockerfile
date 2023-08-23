FROM ghcr.io/airlift/jvmkill:latest AS jvmkill

FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:17.0.8

RUN yum install -y less

USER hadoop

WORKDIR /home/hadoop/

ENV TRINO_HOME=/home/hadoop/trino

RUN   wget https://repo1.maven.org/maven2/io/trino/trino-server/424/trino-server-424.tar.gz && tar -zxvf trino-server-424.tar.gz -C /home/hadoop/ \
    && rm -f trino-server-424.tar.gz && mv trino-server-424  trino  && wget https://repo1.maven.org/maven2/io/trino/trino-cli/424/trino-cli-424-executable.jar \
    && mv trino-cli-424-executable.jar /home/hadoop/trino/bin/trino-cli && chmod +x /home/hadoop/trino/bin/trino-cli

COPY --chown=trino:trino --from=jvmkill /libjvmkill.so /home/hadoop/trino/bin

ENV PATH=$PATH:$TRINO_HOME/bin
