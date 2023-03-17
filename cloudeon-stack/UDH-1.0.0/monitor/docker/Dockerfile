FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141

WORKDIR /opt

ENV PROMETHEUS_VERSION=2.17.2
ENV ALERTMANAGER_VERSION=0.23.0
ENV GRAFANA_VERSION=9.1.6
ENV NODE_EXPORTER_VERSION=1.5.0

RUN wget https://github.com/prometheus/prometheus/releases/download/v$PROMETHEUS_VERSION/prometheus-$PROMETHEUS_VERSION.linux-amd64.tar.gz && tar -zxvf prometheus-$PROMETHEUS_VERSION.linux-amd64.tar.gz -C /opt \
    && rm -f prometheus-$PROMETHEUS_VERSION.linux-amd64.tar.gz && mv prometheus-$PROMETHEUS_VERSION.linux-amd64  prometheus-latest  \
     && wget https://github.com/prometheus/alertmanager/releases/download/v$ALERTMANAGER_VERSION/alertmanager-$ALERTMANAGER_VERSION.linux-amd64.tar.gz \
    && tar -zxvf alertmanager-$ALERTMANAGER_VERSION.linux-amd64.tar.gz -C /opt/   && rm -f alertmanager-$ALERTMANAGER_VERSION.linux-amd64.tar.gz && mv alertmanager-$ALERTMANAGER_VERSION.linux-amd64  alertmanager-latest \
     && wget https://dl.grafana.com/oss/release/grafana-$GRAFANA_VERSION.linux-amd64.tar.gz \
    && tar -zxvf grafana-$GRAFANA_VERSION.linux-amd64.tar.gz -C /opt/   && rm -f grafana-$GRAFANA_VERSION.linux-amd64.tar.gz && mv grafana-$GRAFANA_VERSION  grafana-latest \
      && wget https://github.com/prometheus/node_exporter/releases/download/v$NODE_EXPORTER_VERSION/node_exporter-$NODE_EXPORTER_VERSION.linux-amd64.tar.gz \
    && tar -zxvf node_exporter-$NODE_EXPORTER_VERSION.linux-amd64.tar.gz -C /opt/   && rm -f node_exporter-$NODE_EXPORTER_VERSION.linux-amd64.tar.gz && mv node_exporter-$NODE_EXPORTER_VERSION.linux-amd64  nodeexporter-latest



ENV PROMETHEUS_HOME=/opt/prometheus-latest/
ENV ALERTMANAGER_HOME=/opt/alertmanager-latest/
ENV GRAFANA_HOME=/opt/grafana-latest
ENV NODE_EXPORTER_HOME=/opt/nodeexporter-latest

ENV PATH=$PATH:$PROMETHEUS_HOME:$ALERTMANAGER_HOME:$GRAFANA_HOME/bin:$NODE_EXPORTER_HOME
