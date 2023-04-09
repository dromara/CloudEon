docker build -f Dockerfile -t kafka:2.8.2 .
docker tag   kafka:2.8.2  registry.cn-hangzhou.aliyuncs.com/udh/kafka:2.8.2
docker push registry.cn-hangzhou.aliyuncs.com/udh/kafka:2.8.2

docker tag   registry.cn-hangzhou.aliyuncs.com/udh/kafka:2.8.2  registry.mufankong.top/udh/kafka:2.8.2
docker push  registry.mufankong.top/udh/kafka:2.8.2
