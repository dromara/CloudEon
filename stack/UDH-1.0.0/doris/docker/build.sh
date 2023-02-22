docker build -f Dockerfile -t doris:1.2.1 .
docker tag doris:1.2.1 registry.cn-hangzhou.aliyuncs.com/udh/doris:1.2.1
docker push registry.cn-hangzhou.aliyuncs.com/udh/doris:1.2.1

#docker tag doris:1.2.1 registry.mufankong.top/udh/doris:1.2.1
#docker push  registry.mufankong.top/udh/doris:1.2.1

