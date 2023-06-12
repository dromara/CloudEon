
FROM openjdk:11-jre-slim
WORKDIR /usr/src/app

ARG ZIP_RELEASE_NAME="cloudeon-assembly-v1.0.0-release"

RUN apt update && apt install unzip

COPY cloudeon-assembly/target/${ZIP_RELEASE_NAME}.zip .

RUN unzip ${ZIP_RELEASE_NAME}.zip

RUN ls /usr/src/app/

EXPOSE 7700

ENTRYPOINT ["/bin/bash", "/usr/src/app/cloudeon-assembly-v1.0.0/bin/server.sh","start"]
