docker build --build-arg "https_proxy=http://my.win:7890" -t registry.cn-guangzhou.aliyuncs.com/bigdata200/kafka:2.8.2 .
docker push  registry.cn-guangzhou.aliyuncs.com/bigdata200/kafka:2.8.2
