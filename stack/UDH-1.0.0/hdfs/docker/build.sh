

docker build -f Dockerfile -t hadoop:3.3.4 .
docker tag  hadoop:3.3.4  registry.cn-hangzhou.aliyuncs.com/udh/hadoop:3.3.4
docker push registry.cn-hangzhou.aliyuncs.com/udh/hadoop:3.3.4

#docker tag  hadoop:3.3.4  registry.mufankong.top/udh/hadoop:3.3.4
#docker push  registry.mufankong.top/udh/hadoop:3.3.4
