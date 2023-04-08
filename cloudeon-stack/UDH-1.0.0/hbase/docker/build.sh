docker build -f Dockerfile -t hbase:2.4.16 .
 docker tag hbase:2.4.16  registry.cn-hangzhou.aliyuncs.com/udh/hbase:2.4.16
 docker push  registry.cn-hangzhou.aliyuncs.com/udh/hbase:2.4.16

 docker tag registry.cn-hangzhou.aliyuncs.com/udh/hbase:2.4.16 registry.mufankong.top/udh/hbase:2.4.16
 docker push  registry.mufankong.top/udh/hbase:2.4.16