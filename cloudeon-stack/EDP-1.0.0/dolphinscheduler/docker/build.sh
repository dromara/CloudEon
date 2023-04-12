

docker build -f Dockerfile -t dolphinscheduler:3.0.4 .
docker tag   dolphinscheduler:3.0.4  registry.cn-hangzhou.aliyuncs.com/udh/dolphinscheduler:3.0.4
docker push registry.cn-hangzhou.aliyuncs.com/udh/dolphinscheduler:3.0.4

docker tag   registry.cn-hangzhou.aliyuncs.com/udh/dolphinscheduler:3.0.4  registry.mufankong.top/udh/dolphinscheduler:3.0.4
docker push  registry.mufankong.top/udh/dolphinscheduler:3.0.4
