docker build -f Dockerfile -t hue:4.11.0 .
docker tag   hue:4.11.0  registry.cn-hangzhou.aliyuncs.com/udh/hue:4.11.0
docker push registry.cn-hangzhou.aliyuncs.com/udh/hue:4.11.0

