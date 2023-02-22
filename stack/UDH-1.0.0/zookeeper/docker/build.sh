docker build -f Dockerfile -t zookeeper:3.5.9 .
#docker tag zookeeper:3.5.9 registry.cn-hangzhou.aliyuncs.com/udh/zookeeper:3.5.9
#docker push registry.cn-hangzhou.aliyuncs.com/udh/zookeeper:3.5.9

docker tag zookeeper:3.5.9 registry.mufankong.top/udh/zookeeper:3.5.9
docker push  registry.mufankong.top/udh/zookeeper:3.5.9

