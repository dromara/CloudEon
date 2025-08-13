FROM registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:latest

ENV DORIS_HOME=/opt/doris
ENV PATH=$PATH:$DORIS_HOME/fe/bin:$DORIS_HOME/be/bin

WORKDIR /tmp

# 声明架构参数 (由buildx自动注入)
ARG TARGETARCH
ARG NOAVX2
RUN set -ex; \
        if [ "$TARGETARCH" = "amd64" ]; then \
            if [ "$NOAVX2" = "NOAVX2" ]; then \
                DORIS_URL="https://apache-doris-releases.oss-accelerate.aliyuncs.com/apache-doris-2.1.8.1-bin-x64-noavx2.tar.gz"; \
            else \
                DORIS_URL="https://apache-doris-releases.oss-accelerate.aliyuncs.com/apache-doris-2.1.8.1-bin-x64.tar.gz"; \
            fi; \
        elif [ "$TARGETARCH" = "arm64" ]; then \
            DORIS_URL="https://apache-doris-releases.oss-accelerate.aliyuncs.com/apache-doris-2.1.8.1-bin-arm64.tar.gz"; \
        else \
            echo "ERROR: Unsupported architecture $TARGETARCH"; exit 1; \
        fi; \
    wget $DORIS_URL \
    && tar -xvf apache-doris-*.tar.gz -C /opt \
    && rm -f apache-doris-*.tar.gz && mv /opt/apache-doris-*  $DORIS_HOME \
    && apt-get update \
    && apt-get install -y mariadb-client

WORKDIR $DORIS_HOME
