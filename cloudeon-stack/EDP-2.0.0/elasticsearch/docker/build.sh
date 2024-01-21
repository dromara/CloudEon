docker build -t registry.cn-guangzhou.aliyuncs.com/bigdata200/elasticsearch:7.16.3 .
docker push  registry.cn-guangzhou.aliyuncs.com/bigdata200/elasticsearch:7.16.3

docker pull prometheuscommunity/elasticsearch-exporter:v1.7.0
docker tag prometheuscommunity/elasticsearch-exporter:v1.7.0 registry.cn-guangzhou.aliyuncs.com/bigdata200/elasticsearch_exporter:v1.7.0
docker push registry.cn-guangzhou.aliyuncs.com/bigdata200/elasticsearch_exporter:v1.7.0
