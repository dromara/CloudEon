

docker build -f Dockerfile -t dolphinscheduler:3.0.5 .
docker tag   dolphinscheduler:3.0.5  registry.cn-hangzhou.aliyuncs.com/udh/dolphinscheduler:3.0.5
docker push registry.cn-hangzhou.aliyuncs.com/udh/dolphinscheduler:3.0.5

docker tag   registry.cn-hangzhou.aliyuncs.com/udh/dolphinscheduler:3.0.5  registry.mufankong.top/udh/dolphinscheduler:3.0.5
docker push  registry.mufankong.top/udh/dolphinscheduler:3.0.5
