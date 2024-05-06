apiVersion: v1
kind: ConfigMap
metadata:
  name: "${serviceFullName}-grafana-dashboard"
  labels:
    grafana_dashboard: "1"
    serviceName: "${serviceFullName}"
  annotations:
    folder: "${serviceFullName}"
data:
  k8s-dashboard.json: |
    <#noparse>
    {
      "annotations": {
        "list": [
          {
            "builtIn": 1,
            "datasource": {
              "type": "datasource",
              "uid": "grafana"
            },
            "enable": true,
            "hide": true,
            "iconColor": "rgba(0, 211, 255, 1)",
            "name": "Annotations & Alerts",
            "type": "dashboard"
          }
        ]
      },
      "description": "Hbase  2.x in kubernetes using prometheus jmx eporter",
      "editable": true,
      "fiscalYearStartMonth": 0,
      "graphTooltip": 1,
      "id": 43,
      "links": [],
      "liveNow": false,
      "panels": [
        {
          "collapsed": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 0
          },
          "id": 8,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "refId": "A"
            }
          ],
          "title": "General",
          "type": "row"
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Number of region servers managed under given namenode instance. Also allows to detect active master rnode change",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 0,
            "y": 1
          },
          "hiddenSeries": false,
          "id": 2,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "hadoop_hbase_numregionservers{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pod}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Region Servers",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "decimals": 0,
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 6,
            "y": 1
          },
          "hiddenSeries": false,
          "id": 4,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "hadoop_hbase_deadserveropenregions{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Dead Regions",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "decimals": 0,
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 12,
            "y": 1
          },
          "hiddenSeries": false,
          "id": 20,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "hadoop_hbase_ritcount{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "B"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Regions in transition",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 18,
            "y": 1
          },
          "hiddenSeries": false,
          "id": 5,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_exceptions{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Exceptions rate",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 0,
            "y": 6
          },
          "hiddenSeries": false,
          "id": 22,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_totalrequestcount{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "B"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Total Requests",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 6,
            "y": 6
          },
          "hiddenSeries": false,
          "id": 18,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_receivedbytes{pod=~\".*master.*\",namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "rx {{ instance }} {{ pod }}",
              "refId": "B"
            },
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_sentbytes{pod=~\".*master.*\",namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "tx {{ instance }} {{ pod }}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "rx/tx master",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 12,
            "y": 6
          },
          "hiddenSeries": false,
          "id": 82,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_receivedbytes{pod=~\".*region.*\",namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "rx {{ instance }} {{ pod }}",
              "refId": "B"
            },
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_sentbytes{pod=~\".*region.*\",namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "tx {{ instance }} {{ pod }}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "rx/tx region",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 18,
            "y": 6
          },
          "hiddenSeries": false,
          "id": 3,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "hadoop_hbase_averageload{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pod}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Average Load",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "decimals": 2,
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "collapsed": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 11
          },
          "id": 10,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "refId": "A"
            }
          ],
          "title": "Regions",
          "type": "row"
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 0,
            "y": 12
          },
          "hiddenSeries": false,
          "id": 6,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_flushedcellssize{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "flushed cells size rate",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 6,
            "y": 12
          },
          "hiddenSeries": false,
          "id": 19,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_readrequestcount{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "B"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "read request rate",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 12,
            "y": 12
          },
          "hiddenSeries": false,
          "id": 17,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "hadoop_hbase_hlogfilesize{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "B"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "hlog file size",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 18,
            "y": 12
          },
          "hiddenSeries": false,
          "id": 24,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_flushedmemstorebytes{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "flushed memstore rate",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 0,
            "y": 17
          },
          "hiddenSeries": false,
          "id": 11,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_flushedcellscount{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "flushed cells count rate",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 6,
            "x": 6,
            "y": 17
          },
          "hiddenSeries": false,
          "id": 12,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_flushedmemstorebytes{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{ instance }} {{ pod }}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "flushed memstore rate",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "collapsed": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 22
          },
          "id": 26,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "refId": "A"
            }
          ],
          "title": "num_ops",
          "type": "row"
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Rate of number of operations ",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 5,
            "w": 4,
            "x": 0,
            "y": 23
          },
          "hiddenSeries": false,
          "id": 23,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "maxPerRow": 6,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "repeat": "num_ops",
          "repeatDirection": "h",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(hadoop_hbase_[[num_ops]]_num_ops{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{instance}} {{pod}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "$num_ops rate",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "opm",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "collapsed": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 78
          },
          "id": 139,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "refId": "A"
            }
          ],
          "title": "Java",
          "type": "row"
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Used bytes of a given JVM memory area.",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 0,
            "y": 79
          },
          "hiddenSeries": false,
          "id": 149,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_memory_bytes_used{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{area}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_memory_bytes_used",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Committed (bytes) of a given JVM memory area.",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 6,
            "y": 79
          },
          "hiddenSeries": false,
          "id": 150,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_memory_bytes_committed{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{area}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_memory_bytes_committed",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Max (bytes) of a given JVM memory area.",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 12,
            "y": 79
          },
          "hiddenSeries": false,
          "id": 151,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_memory_bytes_max{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{area}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_memory_bytes_max",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Initial bytes of a given JVM memory area.",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 18,
            "y": 79
          },
          "hiddenSeries": false,
          "id": 152,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_memory_bytes_init{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{area}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_memory_bytes_init",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Used bytes of a given JVM memory pool.",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 0,
            "y": 85
          },
          "hiddenSeries": false,
          "id": 153,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_memory_pool_bytes_used{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_memory_pool_bytes_used",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Committed bytes of a given JVM memory pool.",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 6,
            "y": 85
          },
          "hiddenSeries": false,
          "id": 154,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_memory_pool_bytes_committed{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_memory_pool_bytes_committed",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Max bytes of a given JVM memory pool.",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 12,
            "y": 85
          },
          "hiddenSeries": false,
          "id": 155,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_memory_pool_bytes_max{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_memory_pool_bytes_max",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Initial bytes of a given JVM memory pool.",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 18,
            "y": 85
          },
          "hiddenSeries": false,
          "id": 156,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_memory_pool_bytes_init{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_memory_pool_bytes_init",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Bytes capacity of a given JVM buffer pool.\ngauge",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 0,
            "y": 91
          },
          "hiddenSeries": false,
          "id": 157,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_buffer_pool_capacity_bytes{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_buffer_pool_capacity_bytes",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Used buffers of a given JVM buffer pool.\ngauge",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 6,
            "y": 91
          },
          "hiddenSeries": false,
          "id": 158,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_buffer_pool_used_buffers{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_buffer_pool_used_buffers",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "bytes",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Started thread count of a JVM\ncounter",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 12,
            "y": 91
          },
          "hiddenSeries": false,
          "id": 162,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_threads_started_total{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_threads_started_total",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Daemon thread count of a JVM\ngauge",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 18,
            "y": 91
          },
          "hiddenSeries": false,
          "id": 160,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_threads_daemon{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_threads_daemon",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Current thread count of a JVM\ngauge",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 0,
            "y": 97
          },
          "hiddenSeries": false,
          "id": 159,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_threads_current{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_threads_current",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Peak thread count of a JVM\ngauge",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 6,
            "y": 97
          },
          "hiddenSeries": false,
          "id": 161,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_threads_peak{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_threads_peak",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Cycles of JVM-threads that are in deadlock waiting to acquire object monitors\ngauge",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 12,
            "y": 97
          },
          "hiddenSeries": false,
          "id": 165,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_threads_deadlocked_monitor{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_threads_deadlocked_monitor",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "java.lang.management.MemoryUsage (java.lang<type=MemoryPool, name=Code Cache><Usage>committed)",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 18,
            "y": 97
          },
          "hiddenSeries": false,
          "id": 167,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "java_lang_memorypool_usage_committed{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{name}} {{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "java_lang_memorypool_usage_committed",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Started thread count of a JVM\nrate",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 0,
            "y": 103
          },
          "hiddenSeries": false,
          "id": 163,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "rate(jvm_threads_started_total{namespace=~\"$namespace\",instance=~\"$instance\"}[1m])",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_threads_started_total rate",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Cycles of JVM-threads that are in deadlock waiting to acquire object monitors or ownable synchronizers\ngauge",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 6,
            "y": 103
          },
          "hiddenSeries": false,
          "id": 164,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "jvm_threads_deadlocked{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "jvm_threads_deadlocked",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "Valid (java.lang<type=MemoryPool, name=Code Cache><>Valid)",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 12,
            "y": 103
          },
          "hiddenSeries": false,
          "id": 166,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "java_lang_memorypool_valid{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{name}} {{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "java_lang_memorypool_valid",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "java.lang.management.MemoryUsage (java.lang<type=GarbageCollector, name=ParNew, key=Par Survivor Space><LastGcInfo, memoryUsageAfterGc>used)",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 18,
            "y": 103
          },
          "hiddenSeries": false,
          "id": 168,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "java_lang_garbagecollector_lastgcinfo_memoryusageaftergc_used{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{name}} {{key}} {{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "java_lang_garbagecollector_lastgcinfo_memoryusageaftergc_used",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "UsageThresholdExceeded (java.lang<type=MemoryPool, name=Code Cache><>UsageThresholdExceeded)",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 0,
            "y": 109
          },
          "hiddenSeries": false,
          "id": 169,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "java_lang_memorypool_usagethresholdexceeded{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "java_lang_memorypool_usagethresholdexceeded",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "java.lang.management.MemoryUsage (java.lang<type=MemoryPool, name=Par Eden Space><CollectionUsage>used)",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 6,
            "y": 109
          },
          "hiddenSeries": false,
          "id": 170,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "java_lang_memorypool_collectionusage_used{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "java_lang_memorypool_collectionusage_used",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "java.lang.management.MemoryUsage (java.lang<type=MemoryPool, name=Code Cache><Usage>used)",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 12,
            "y": 109
          },
          "hiddenSeries": false,
          "id": 171,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "java_lang_memorypool_usage_used{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "java_lang_memorypool_usage_used",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "java.lang.management.MemoryUsage (java.lang<type=MemoryPool, name=Code Cache><PeakUsage>committed)",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 18,
            "y": 109
          },
          "hiddenSeries": false,
          "id": 172,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "java_lang_memorypool_peakusage_committed{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "java_lang_memorypool_peakusage_committed",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "prometheus"
          },
          "description": "CollectionUsageThresholdExceeded (java.lang<type=MemoryPool, name=Par Eden Space><>CollectionUsageThresholdExceeded)",
          "fieldConfig": {
            "defaults": {
              "links": [],
              "unitScale": true
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 0,
            "y": 115
          },
          "hiddenSeries": false,
          "id": 173,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": false,
            "total": false,
            "values": false
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "prometheus"
              },
              "expr": "java_lang_memorypool_collectionusagethresholdexceeded{namespace=~\"$namespace\",instance=~\"$instance\"}",
              "interval": "",
              "legendFormat": "{{pool}} {{instance}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "java_lang_memorypool_collectionusagethresholdexceeded",
          "tooltip": {
            "shared": true,
            "sort": 2,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "mode": "time",
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "logBase": 1,
              "min": "0",
              "show": true
            },
            {
              "format": "short",
              "logBase": 1,
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        }
      ],
      "refresh": "10s",
      "schemaVersion": 39,
      "tags": [
        "hbase"
      ],
      "templating": {
        "list": [
          {
            "current": {
              "selected": false,
              "text": "default",
              "value": "default"
            },
            "hide": 2,
            "includeAll": false,
            "label": "Data source",
            "multi": false,
            "name": "datasource",
            "options": [],
            "query": "prometheus",
            "refresh": 1,
            "regex": "",
            "skipUrlSync": false,
            "type": "datasource"
          },
          {
            "allValue": ".*",
            "current": {
              "selected": false,
              "text": "${namespace}",
              "value": "${namespace}"
            },
            "datasource": {
              "uid": "$datasource"
            },
            "definition": "",
            "hide": 0,
            "includeAll": true,
            "label": "Namespace",
            "multi": true,
            "name": "namespace",
            "options": [],
            "query": "label_values(hadoop_hbase_exceptions, namespace)",
            "refresh": 2,
            "regex": "",
            "skipUrlSync": false,
            "sort": 1,
            "tagValuesQuery": "",
            "tagsQuery": "",
            "type": "query",
            "useTags": false
          },
          {
            "current": {
              "selected": true,
              "text": [
                "All"
              ],
              "value": [
                "$__all"
              ]
            },
            "datasource": {
              "type": "prometheus",
              "uid": "${datasource}"
            },
            "definition": "label_values(hadoop_hbase_exceptions, instance)",
            "hide": 0,
            "includeAll": true,
            "multi": true,
            "name": "instance",
            "options": [],
            "query": "label_values(hadoop_hbase_exceptions, instance)",
            "refresh": 2,
            "regex": "",
            "skipUrlSync": false,
            "sort": 1,
            "tagValuesQuery": "",
            "tagsQuery": "",
            "type": "query",
            "useTags": false
          },
          {
            "current": {
              "selected": true,
              "text": [
                "All"
              ],
              "value": [
                "$__all"
              ]
            },
            "datasource": {
              "type": "prometheus",
              "uid": "${datasource}"
            },
            "definition": "metrics(num_ops)",
            "hide": 0,
            "includeAll": true,
            "multi": true,
            "name": "num_ops",
            "options": [],
            "query": "metrics(num_ops)",
            "refresh": 1,
            "regex": "/hbase_([a-zAZ]+)_num_ops/",
            "skipUrlSync": false,
            "sort": 1,
            "tagValuesQuery": "",
            "tagsQuery": "",
            "type": "query",
            "useTags": false
          }
        ]
      },
      "time": {
        "from": "now-3h",
        "to": "now"
      },
      "timepicker": {
        "refresh_intervals": [
          "5s",
          "10s",
          "30s",
          "1m",
          "5m",
          "15m",
          "30m",
          "1h",
          "2h",
          "1d"
        ]
      },
      "timezone": "browser",
      "title": "HBase",
      "uid": "hbase001",
      "version": 1,
      "weekStart": ""
    }
    </#noparse>

