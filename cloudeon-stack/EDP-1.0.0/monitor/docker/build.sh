

#docker build -f Dockerfile -t monitor:1.0.0 .
#docker tag monitor:1.0.0 registry.cn-hangzhou.aliyuncs.com/udh/monitor:1.0.0
#docker push registry.cn-hangzhou.aliyuncs.com/udh/monitor:1.0.0
docker pull registry.cn-hangzhou.aliyuncs.com/udh/monitor:1.0.0

docker tag  registry.cn-hangzhou.aliyuncs.com/udh/monitor:1.0.0  registry.mufankong.top/udh/monitor:1.0.0
docker push  registry.mufankong.top/udh/monitor:1.0.0
