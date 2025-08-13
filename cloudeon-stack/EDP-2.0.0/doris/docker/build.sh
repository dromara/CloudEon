# 根据机器是否支持avx2指令集决定要使用的镜像，不支持则镜像tag后面携带 noavx2
cat /proc/cpuinfo | grep avx2

# dockerfile默认使用的是 avx2
docker buildx build --platform linux/amd64,linux/arm64 -f Dockerfile -t registry.cn-guangzhou.aliyuncs.com/bigdata200/doris:2.1.8.1 --push .

# 如果要使用 noavx2 或使用不同版本，在这里指定下载链接。注意：NOAVX2 只支持x86架构
docker buildx build --platform linux/amd64 --build-arg NOAVX2='NOAVX2' -f Dockerfile -t registry.cn-guangzhou.aliyuncs.com/bigdata200/doris:2.1.8.1-noavx2 --push .
