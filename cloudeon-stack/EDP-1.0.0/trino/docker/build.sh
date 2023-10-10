docker build -f Dockerfile -t trino:424 .
docker tag   trino:424  registry.cn-hangzhou.aliyuncs.com/udh/trino:424
docker push registry.cn-hangzhou.aliyuncs.com/udh/trino:424

# docker tag   registry.cn-hangzhou.aliyuncs.com/udh/kyuubi:1.7.0  registry.mufankong.top/udh/kyuubi:1.7.0
# docker push  registry.mufankong.top/udh/kyuubi:1.7.0
