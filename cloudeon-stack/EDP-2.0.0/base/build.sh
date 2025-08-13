docker buildx create --use --name multi-platform-builder \
  --driver docker-container \
  --driver-opt env.http_proxy="http://192.168.200.192:8011" \
  --driver-opt env.https_proxy="http://192.168.200.192:8011" \
  --driver-opt '"env.no_proxy=localhost,127.0.0.1,registry.cn-guangzhou.aliyuncs.com"'


docker buildx build --platform linux/amd64,linux/arm64 -f Dockerfile_ubuntu_jdk8 -t registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:8-ubuntu --push .
docker buildx build --platform linux/amd64,linux/arm64 -f Dockerfile -t registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:latest --push .
docker buildx build --platform linux/amd64,linux/arm64 -f DockerfileJdk17 -t registry.cn-guangzhou.aliyuncs.com/bigdata200/jdk:17 --push .
