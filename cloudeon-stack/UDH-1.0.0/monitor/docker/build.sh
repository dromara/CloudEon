

docker build -f Dockerfile -t monitor:1.0.0 .
docker tag monitor:1.0.0 registry.cn-hangzhou.aliyuncs.com/udh/monitor:1.0.0
docker push registry.cn-hangzhou.aliyuncs.com/udh/monitor:1.0.0

#docker tag  hadoop:3.3.4  registry.mufankong.top/udh/hadoop:3.3.4
#docker push  registry.mufankong.top/udh/hadoop:3.3.4
