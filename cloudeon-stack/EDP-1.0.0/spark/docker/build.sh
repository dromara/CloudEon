

docker build -f Dockerfile -t spark:3.2.3 .
docker tag   spark:3.2.3  registry.cn-hangzhou.aliyuncs.com/udh/spark:3.2.3
docker push   registry.cn-hangzhou.aliyuncs.com/udh/spark:3.2.3

 docker tag   registry.cn-hangzhou.aliyuncs.com/udh/spark:3.2.3  registry.mufankong.top/udh/spark:3.2.3
 docker push  registry.mufankong.top/udh/spark:3.2.3
