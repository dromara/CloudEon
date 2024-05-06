docker build --build-arg all_proxy="http://my.win:7890" --build-arg http_proxy="http://my.win:7890" --build-arg https_proxy="http://my.win:7890" -f Dockerfile -t registry.cn-guangzhou.aliyuncs.com/bigdata200/spark:3.2.3  .
docker push  registry.cn-guangzhou.aliyuncs.com/bigdata200/spark:3.2.3
