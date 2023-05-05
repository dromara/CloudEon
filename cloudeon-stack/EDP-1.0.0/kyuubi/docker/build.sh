docker build -f Dockerfile -t kyuubi:1.7.0 .
docker tag   kyuubi:1.7.0  registry.cn-hangzhou.aliyuncs.com/udh/kyuubi:1.7.0
docker push registry.cn-hangzhou.aliyuncs.com/udh/kyuubi:1.7.0

# docker tag   registry.cn-hangzhou.aliyuncs.com/udh/kyuubi:1.7.0  registry.mufankong.top/udh/kyuubi:1.7.0
# docker push  registry.mufankong.top/udh/kyuubi:1.7.0
