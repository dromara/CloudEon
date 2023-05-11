

docker build -f Dockerfile -t kylin:5.0.0-alpha .
docker tag   kylin:5.0.0-alpha  registry.cn-hangzhou.aliyuncs.com/udh/kylin:5.0.0-alpha
docker push   registry.cn-hangzhou.aliyuncs.com/udh/kylin:5.0.0-alpha

#  docker tag   registry.cn-hangzhou.aliyuncs.com/udh/kylin:5.0.0-alpha  registry.mufankong.top/udh/kylin:5.0.0-alpha
#  docker push  registry.mufankong.top/udh/kylin:5.0.0-alpha

