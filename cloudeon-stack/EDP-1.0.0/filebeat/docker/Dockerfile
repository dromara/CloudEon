FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

WORKDIR /opt



RUN wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.16.3-linux-x86_64.tar.gz && tar -zxvf filebeat-7.16.3-linux-x86_64.tar.gz -C /opt \
    && rm -f filebeat-7.16.3-linux-x86_64.tar.gz && mv filebeat-7.16.3-linux-x86_64  filebeat


ENV PATH=$PATH:/opt/filebeat/
