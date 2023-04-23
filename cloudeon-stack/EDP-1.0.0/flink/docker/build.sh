

docker build -f Dockerfile -t flink:1.15.4 .
docker tag   flink:1.15.4  registry.cn-hangzhou.aliyuncs.com/udh/flink:1.15.4
docker push   registry.cn-hangzhou.aliyuncs.com/udh/flink:1.15.4

 docker tag   registry.cn-hangzhou.aliyuncs.com/udh/flink:1.15.4  registry.mufankong.top/udh/flink:1.15.4
 docker push  registry.mufankong.top/udh/flink:1.15.4

