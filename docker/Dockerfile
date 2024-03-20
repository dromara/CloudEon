FROM maven:3.8.6-jdk-8

ARG BUILD_DATE
ARG CLOUDEON_VERSION=v0.0.0

ENV CLOUDEON_HOME=/opt/cloudeon \
    CLOUDEON_VERSION=${CLOUDEON_VERSION}

# 时区
ENV TZ Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone

ADD cloudeon-assembly-*.tar.gz /opt/

RUN mv /opt/cloudeon-assembly-*  $CLOUDEON_HOME

# 健康检查
HEALTHCHECK CMD curl -X GET -f http://localhost:7700 || exit 1

EXPOSE 7700

WORKDIR ${CLOUDEON_HOME}

ENTRYPOINT ["/bin/bash", "bin/server.sh","start"]
