

docker build -f Dockerfile -t dinky:0.7.5 .
docker tag   dinky:0.7.5  registry.cn-hangzhou.aliyuncs.com/udh/dinky:0.7.5
docker push registry.cn-hangzhou.aliyuncs.com/udh/dinky:0.7.5

docker tag   registry.cn-hangzhou.aliyuncs.com/udh/dinky:0.7.5  registry.mufankong.top/udh/dinky:0.7.5
docker push  registry.mufankong.top/udh/dinky:0.7.5


docker tag  dinky:0.7.5 172.16.129.68:5000/dinky:0.7.5