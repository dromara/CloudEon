docker build -f Dockerfile -t doris:1.1.5 .
docker tag doris:1.1.5 registry.cn-hangzhou.aliyuncs.com/udh/doris:1.1.5
docker push registry.cn-hangzhou.aliyuncs.com/udh/doris:1.1.5

#docker tag doris:1.1.5 registry.mufankong.top/udh/doris:1.1.5
#docker push  registry.mufankong.top/udh/doris:1.1.5
