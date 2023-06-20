

docker build -f Dockerfile -t seatunnel:2.3.1 .
docker tag   seatunnel:2.3.1  registry.cn-hangzhou.aliyuncs.com/udh/seatunnel:2.3.1
docker push   registry.cn-hangzhou.aliyuncs.com/udh/seatunnel:2.3.1

 docker tag   registry.cn-hangzhou.aliyuncs.com/udh/seatunnel:2.3.1  registry.mufankong.top/udh/seatunnel:2.3.1
 docker push  registry.mufankong.top/udh/seatunnel:2.3.1

