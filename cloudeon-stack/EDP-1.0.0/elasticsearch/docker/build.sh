docker build -f Dockerfile -t elasticsearch:7.16.3 .
docker tag  elasticsearch:7.16.3  registry.cn-hangzhou.aliyuncs.com/udh/elasticsearch:7.16.3
docker push registry.cn-hangzhou.aliyuncs.com/udh/elasticsearch:7.16.3