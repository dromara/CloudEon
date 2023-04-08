

docker build -f Dockerfile -t hive:3.1.3 .
docker tag   hive:3.1.3  registry.cn-hangzhou.aliyuncs.com/udh/hive:3.1.3
docker push registry.cn-hangzhou.aliyuncs.com/udh/hive:3.1.3

docker tag   registry.cn-hangzhou.aliyuncs.com/udh/hive:3.1.3  registry.mufankong.top/udh/hive:3.1.3
docker push  registry.mufankong.top/udh/hive:3.1.3
