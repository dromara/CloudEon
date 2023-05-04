
FROM maven:3.8.6-jdk-8

ARG BUILD_DATE
ARG CLOUDEON_VERSION=v0.0.0

ENV CLOUDEON_HOME	/usr/local/cloudeon

WORKDIR ${CLOUDEON_HOME}

# 时区
ENV TZ Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone

RUN mkdir -p ${CLOUDEON_HOME}

COPY cloudeon-assembly-${CLOUDEON_VERSION}-release .
# 健康检查
HEALTHCHECK CMD curl -X POST -f http://localhost:7700 || exit 1

EXPOSE 7700


ENTRYPOINT ["/bin/bash", "./bin/server.sh","start"]