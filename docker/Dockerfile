FROM maven:3.8.6-jdk-8 AS build

WORKDIR usr/src/app

ENV TZ Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone

COPY . ./

RUN mvn clean install -DskipTests -Dlicense.skip=true 

# Usage Stage...

FROM openjdk:11-jre-slim

RUN apt update && apt install unzip

ARG ZIP_RELEASE_NAME="cloudeon-assembly-v1.0.0-release"

WORKDIR /usr/src/app

EXPOSE 7700

COPY --from=build /usr/src/app/target/${ZIP_RELEASE_NAME}.zip .

RUN unzip -v ${ZIP_RELEASE_NAME}.zip

ENTRYPOINT ["/bin/bash", "/usr/src/app/cloudeon-assembly-v1.0.0/bin/server.sh","start"]
