docker build -f Dockerfile -t amoro:0.5.0 .

docker tag amoro:0.5.0 registry.cn-hangzhou.aliyuncs.com/udh/amoro:0.5.0
docker push  registry.cn-hangzhou.aliyuncs.com/udh/amoro:0.5.0

