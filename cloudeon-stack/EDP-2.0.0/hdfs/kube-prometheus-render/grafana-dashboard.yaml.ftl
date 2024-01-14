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
    <#noparse >
    {
      "annotations": {
        "list": [
          {
            "builtIn": 1,
            "datasource": {
              "type": "grafana",
              "uid": "-- Grafana --"
            },
            "enable": true,
            "hide": true,
            "iconColor": "rgba(0, 211, 255, 1)",
            "name": "Annotations & Alerts",
            "type": "dashboard"
          }
        ]
      },
      "description": "",
      "editable": true,
      "fiscalYearStartMonth": 0,
      "graphTooltip": 0,
      "id": 33,
      "links": [
        {
          "asDropdown": false,
          "icon": "external link",
          "includeVars": true,
          "keepTime": true,
          "tags": [
            "apache-hadoop-integration"
          ],
          "targetBlank": false,
          "title": "Other Apache Hadoop dashboards",
          "type": "dashboards",
          "url": ""
        }
      ],
      "liveNow": false,
      "panels": [
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${prometheus_datasource}"
          },
          "description": "The DataNodes current state.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                }
              },
              "mappings": []
            },
            "overrides": []
          },
          "gridPos": {
            "h": 9,
            "w": 12,
            "x": 0,
            "y": 0
          },
          "id": 2,
          "options": {
            "legend": {
              "displayMode": "table",
              "placement": "right",
              "showLegend": true
            },
            "pieType": "pie",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "editorMode": "code",
              "expr": "hadoop_namenode_numlivedatanodes{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}} - live DataNodes",
              "range": true,
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_numdeaddatanodes{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}} - dead DataNodes",
              "refId": "B"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_numstaledatanodes{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}} - stale DataNodes",
              "refId": "C"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_numdecommissioningdatanodes{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}} - decommissioning DataNodes",
              "refId": "D"
            }
          ],
          "title": "DataNode state",
          "type": "piechart"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The storage utilization of the NameNode.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": "percent"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 9,
            "w": 12,
            "x": 12,
            "y": 0
          },
          "id": 3,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "100 * hadoop_namenode_capacityused{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"} / clamp_min(hadoop_namenode_capacitytotal{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}, 1)",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Capacity utilization",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Total number of blocks managed by the NameNode.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 8,
            "x": 0,
            "y": 9
          },
          "id": 4,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_blockstotal{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Total blocks",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Number of blocks reported by DataNodes as missing.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 8,
            "x": 8,
            "y": 9
          },
          "id": 5,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_missingblocks{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Missing blocks",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Displays the total number of volume failures encountered by the Hadoop DataNode.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 8,
            "x": 16,
            "y": 9
          },
          "id": 14,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "increase(hadoop_datanode_volumefailures{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:])",
              "format": "time_series",
              "interval": "1m",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Volume failures",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Number of transactions processed by the NameNode since the last checkpoint.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 0,
            "y": 15
          },
          "id": 7,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_transactionssincelastcheckpoint{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Transactions since last checkpoint",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The recent increase in number of volume failures on all DataNodes.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 12,
            "y": 15
          },
          "id": 8,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "increase(hadoop_namenode_volumefailurestotal{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}[$__interval:])",
              "format": "time_series",
              "interval": "1m",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Volume failures",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Total number of files managed by the NameNode.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 0,
            "y": 21
          },
          "id": 9,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_filestotal{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Total files",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Total load on the NameNode.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 12,
            "y": 21
          },
          "id": 10,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_totalload{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Total load",
          "type": "timeseries"
        },
        {
          "collapsed": false,
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 27
          },
          "id": 11,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A"
            }
          ],
          "title": "DataNodes",
          "type": "row"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Total number of blocks evicted without being read by the Hadoop DataNode.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 8,
            "x": 0,
            "y": 28
          },
          "id": 12,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "increase(hadoop_datanode_ramdiskblocksevictedwithoutread{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:])",
              "format": "time_series",
              "interval": "1m",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Unread blocks evicted",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Total number of blocks removed by the Hadoop DataNode.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 8,
            "x": 8,
            "y": 28
          },
          "id": 13,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "increase(hadoop_datanode_blocksremoved{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:])",
              "format": "time_series",
              "interval": "1m",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Blocks removed",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Number of blocks that are under-replicated.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 0,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "insertNulls": false,
                "lineInterpolation": "linear",
                "lineWidth": 1,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "auto",
                "spanNulls": false,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 8,
            "x": 16,
            "y": 28
          },
          "id": 6,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_namenode_underreplicatedblocks{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"FSNamesystem\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Under-replicated blocks",
          "type": "timeseries"
        }
      ],
      "refresh": false,
      "schemaVersion": 39,
      "tags": [
        "apache-hadoop-integration"
      ],
      "templating": {
        "list": [
          {
            "current": {
              "selected": false,
              "text": "Prometheus",
              "value": "prometheus"
            },
            "hide": 2,
            "includeAll": false,
            "label": "Data Source",
            "multi": false,
            "name": "prometheus_datasource",
            "options": [],
            "query": "prometheus",
            "queryValue": "",
            "refresh": 1,
            "regex": "(?!grafanacloud-usage|grafanacloud-ml-metrics).+",
            "skipUrlSync": false,
            "type": "datasource"
          },
          {
            "allValue": ".+",
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
              "uid": "${prometheus_datasource}"
            },
            "definition": "",
            "hide": 2,
            "includeAll": true,
            "label": "Job",
            "multi": true,
            "name": "job",
            "options": [],
            "query": "label_values(hadoop_namenode_blockstotal,job)",
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
            "allValue": ".+",
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
              "uid": "${prometheus_datasource}"
            },
            "definition": "",
            "hide": 0,
            "includeAll": true,
            "label": "Instance",
            "multi": true,
            "name": "instance",
            "options": [],
            "query": "label_values(hadoop_namenode_blockstotal{job=~\"$job\"}, instance)",
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
            "allValue": ".+",
            "current": {
              "selected": true,
              "text": [
                "bdata"
              ],
              "value": [
                "bdata"
              ]
            },
            "datasource": {
              "uid": "${prometheus_datasource}"
            },
            "definition": "label_values(hadoop_namenode_blockstotal{job=~\"$job\"},namespace)",
            "hide": 0,
            "includeAll": true,
            "label": "namespace",
            "multi": true,
            "name": "namespace",
            "options": [],
            "query": {
              "qryType": 1,
              "query": "label_values(hadoop_namenode_blockstotal{job=~\"$job\"},namespace)",
              "refId": "PrometheusVariableQueryEditor-VariableQuery"
            },
            "refresh": 2,
            "regex": "",
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
        "from": "2024-01-14T03:30:22.538Z",
        "to": "2024-01-14T04:30:22.540Z"
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
        ],
        "time_options": [
          "5m",
          "15m",
          "1h",
          "6h",
          "12h",
          "24h",
          "2d",
          "7d",
          "30d"
        ]
      },
      "timezone": "default",
      "title": "Apache Hadoop Hdfs overview",
      "uid": "hdfs001",
      "version": 3,
      "weekStart": ""
    }
    </#noparse>
