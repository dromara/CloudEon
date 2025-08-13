docker build -t registry.cn-guangzhou.aliyuncs.com/bigdata200/hadoop:3.3.4 .
docker push  registry.cn-guangzhou.aliyuncs.com/bigdata200/hadoop:3.3.4

docker buildx build --platform linux/amd64,linux/arm64 -f Dockerfile -t registry.cn-guangzhou.aliyuncs.com/bigdata200/hadoop:3.3.4 --push .
