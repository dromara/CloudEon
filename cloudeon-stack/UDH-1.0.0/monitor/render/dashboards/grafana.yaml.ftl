apiVersion: 1
providers:
  - name: 'default'
    orgId: 1
    folder: ''
    type: file
    updateIntervalSeconds: 10
    options:
      path: /opt/udh/${service.serviceName}/dashboards/grafana