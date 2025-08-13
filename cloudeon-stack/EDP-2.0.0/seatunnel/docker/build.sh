docker buildx build --platform linux/amd64,linux/arm64 -f Dockerfile -t registry.cn-guangzhou.aliyuncs.com/bigdata200/seatunnel:2.3.7 --push .

docker buildx build --platform linux/amd64,linux/arm64 -f Dockerfile_web -t registry.cn-guangzhou.aliyuncs.com/bigdata200/seatunnel-web:1.0.1 --push .
