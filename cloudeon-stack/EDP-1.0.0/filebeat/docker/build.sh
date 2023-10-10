docker build -f Dockerfile -t filebeat:7.16.3 .
docker tag   filebeat:7.16.3  registry.cn-hangzhou.aliyuncs.com/udh/filebeat:7.16.3
docker push registry.cn-hangzhou.aliyuncs.com/udh/filebeat:7.16.3

