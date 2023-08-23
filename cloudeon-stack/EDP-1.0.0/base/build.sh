docker build -f Jdk-Dockerfile -t jdk:1.8.141 .
 docker tag jdk:1.8.141  registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141
 docker push  registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

 docker build -f Jdk17-Dockerfile -t jdk:17.0.8 .
  docker tag jdk:17.0.8  registry.cn-hangzhou.aliyuncs.com/udh/jdk:17.0.8
  docker push  registry.cn-hangzhou.aliyuncs.com/udh/jdk:17.0.8