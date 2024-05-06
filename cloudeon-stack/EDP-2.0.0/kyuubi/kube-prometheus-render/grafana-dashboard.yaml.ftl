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
            "datasource": "-- Grafana --",
            "enable": true,
            "hide": true,
            "iconColor": "rgba(0, 211, 255, 1)",
            "name": "Annotations & Alerts",
            "target": {
              "limit": 100,
              "matchAny": false,
              "tags": [],
              "type": "dashboard"
            },
            "type": "dashboard"
          }
        ]
      },
      "editable": true,
      "fiscalYearStartMonth": 0,
      "graphTooltip": 0,
      "id": 41,
      "links": [],
      "liveNow": false,
      "panels": [
        {
          "collapsed": false,
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 0
          },
          "id": 58,
          "panels": [],
          "title": "General Information",
          "type": "row"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
              },
              "mappings": [
                {
                  "options": {
                    "null": {
                      "index": 0,
                      "text": "N/A"
                    }
                  },
                  "type": "value"
                }
              ],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "red",
                    "value": null
                  },
                  {
                    "color": "red",
                    "value": 80
                  },
                  {
                    "color": "#EAB839",
                    "value": 90
                  },
                  {
                    "color": "#6ED0E0",
                    "value": 100
                  }
                ]
              },
              "unit": "none",
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 0,
            "y": 1
          },
          "id": 31,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "value",
            "graphMode": "none",
            "justifyMode": "auto",
            "orientation": "horizontal",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showPercentChange": false,
            "textMode": "auto",
            "wideLayout": true
          },
          "pluginVersion": "10.3.1",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "up{job=~\"$job\",namespace=~\"$namespace\"}",
              "hide": false,
              "legendFormat": "__auto",
              "range": true,
              "refId": "A"
            }
          ],
          "title": "Service Count",
          "type": "stat"
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
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
            "h": 8,
            "w": 12,
            "x": 12,
            "y": 1
          },
          "hiddenSeries": false,
          "id": 34,
          "legend": {
            "alignAsTable": true,
            "avg": true,
            "current": true,
            "max": true,
            "min": true,
            "rightSide": false,
            "show": true,
            "sortDesc": true,
            "total": false,
            "values": true
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "maxPerRow": 2,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "repeat": "memarea",
          "repeatDirection": "h",
          "seriesOverrides": [
            {
              "$$hashKey": "object:1328",
              "alias": "Usage %",
              "bars": true,
              "color": "#6d1f62",
              "legend": false,
              "lines": false,
              "yaxis": 2,
              "zindex": -1
            }
          ],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "kyuubi_memory_usage_total_used{job=~\"$job\",namespace=~\"$namespace\",instance=~\"$instance\"}",
              "hide": false,
              "legendFormat": "{{instance}}",
              "range": true,
              "refId": "B"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Memory Usage",
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
              "$$hashKey": "object:1351",
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "$$hashKey": "object:1352",
              "format": "percentunit",
              "label": "",
              "logBase": 1,
              "max": "1",
              "min": "0",
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "collapsed": false,
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 9
          },
          "id": 60,
          "panels": [],
          "title": "JVM Statistics",
          "type": "row"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
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
              "mappings": [
                {
                  "options": {
                    "match": "null",
                    "result": {
                      "text": "N/A"
                    }
                  },
                  "type": "special"
                }
              ],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  },
                  {
                    "color": "red",
                    "value": 100
                  }
                ]
              },
              "unit": "bytes",
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 7,
            "w": 12,
            "x": 0,
            "y": 10
          },
          "id": 32,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "single",
              "sort": "none"
            }
          },
          "pluginVersion": "9.5.2",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "kyuubi_memory_usage_heap_used{job=~\"$job\",namespace=~\"$namespace\",instance=~\"$instance\"}",
              "legendFormat": "{{instance}}",
              "range": true,
              "refId": "A"
            }
          ],
          "title": "Heap Used",
          "type": "timeseries"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
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
              "mappings": [
                {
                  "options": {
                    "match": "null",
                    "result": {
                      "text": "N/A"
                    }
                  },
                  "type": "special"
                }
              ],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  },
                  {
                    "color": "red",
                    "value": 100
                  }
                ]
              },
              "unit": "bytes",
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 7,
            "w": 12,
            "x": 12,
            "y": 10
          },
          "id": 69,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "single",
              "sort": "none"
            }
          },
          "pluginVersion": "9.5.2",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "kyuubi_memory_usage_heap_used{job=~\"$job\",namespace=~\"$namespace\",instance=~\"$instance\"}",
              "legendFormat": "{{instance}}",
              "range": true,
              "refId": "A"
            }
          ],
          "title": "Heap Used",
          "type": "timeseries"
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "${KYUUBI_METRIC_DS}",
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
            "h": 9,
            "w": 8,
            "x": 0,
            "y": 17
          },
          "hiddenSeries": false,
          "id": 62,
          "legend": {
            "alignAsTable": true,
            "avg": true,
            "current": true,
            "max": true,
            "min": true,
            "rightSide": false,
            "show": true,
            "sortDesc": true,
            "total": false,
            "values": true
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "maxPerRow": 2,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "repeatDirection": "h",
          "seriesOverrides": [
            {
              "$$hashKey": "object:1328",
              "alias": "Usage %",
              "bars": true,
              "color": "#6d1f62",
              "legend": false,
              "lines": false,
              "yaxis": 2,
              "zindex": -1
            }
          ],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Metaspace_used{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Metaspace Used",
              "range": true,
              "refId": "B"
            },
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Metaspace_committed{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Metaspace Committed",
              "range": true,
              "refId": "A"
            },
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Metaspace_max{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Metaspace Max",
              "range": true,
              "refId": "C"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Metaspace",
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
              "$$hashKey": "object:1351",
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "$$hashKey": "object:1352",
              "format": "percentunit",
              "label": "",
              "logBase": 1,
              "max": "1",
              "min": "0",
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
          "datasource": "${KYUUBI_METRIC_DS}",
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
            "h": 9,
            "w": 8,
            "x": 8,
            "y": 17
          },
          "hiddenSeries": false,
          "id": 65,
          "legend": {
            "alignAsTable": true,
            "avg": true,
            "current": true,
            "max": true,
            "min": true,
            "rightSide": false,
            "show": true,
            "sortDesc": true,
            "total": false,
            "values": true
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "maxPerRow": 2,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "repeatDirection": "h",
          "seriesOverrides": [
            {
              "$$hashKey": "object:1328",
              "alias": "Usage %",
              "bars": true,
              "color": "#6d1f62",
              "legend": false,
              "lines": false,
              "yaxis": 2,
              "zindex": -1
            }
          ],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Compressed_Class_Space_used{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Compressed Class Used",
              "range": true,
              "refId": "B"
            },
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Compressed_Class_Space_committed{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Compressed Class Committed",
              "range": true,
              "refId": "A"
            },
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Compressed_Class_Space_max{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Compressed Class Max",
              "range": true,
              "refId": "C"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Compressed Class",
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
              "$$hashKey": "object:1351",
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "$$hashKey": "object:1352",
              "format": "percentunit",
              "label": "",
              "logBase": 1,
              "max": "1",
              "min": "0",
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
          "datasource": "${KYUUBI_METRIC_DS}",
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
            "h": 9,
            "w": 8,
            "x": 16,
            "y": 17
          },
          "hiddenSeries": false,
          "id": 66,
          "legend": {
            "alignAsTable": true,
            "avg": true,
            "current": true,
            "max": true,
            "min": true,
            "rightSide": false,
            "show": true,
            "sortDesc": true,
            "total": false,
            "values": true
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "maxPerRow": 2,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "10.3.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "repeatDirection": "h",
          "seriesOverrides": [
            {
              "$$hashKey": "object:1328",
              "alias": "Usage %",
              "bars": true,
              "color": "#6d1f62",
              "legend": false,
              "lines": false,
              "yaxis": 2,
              "zindex": -1
            }
          ],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Code_Cache_used{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Code Cache Used",
              "range": true,
              "refId": "B"
            },
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Code_Cache_committed{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Code Cache Committed",
              "range": true,
              "refId": "A"
            },
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_memory_usage_pools_Code_Cache_max{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Code Cache Max",
              "range": true,
              "refId": "C"
            }
          ],
          "thresholds": [],
          "timeRegions": [],
          "title": "Code Cache",
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
              "$$hashKey": "object:1351",
              "format": "bytes",
              "logBase": 1,
              "show": true
            },
            {
              "$$hashKey": "object:1352",
              "format": "percentunit",
              "label": "",
              "logBase": 1,
              "max": "1",
              "min": "0",
              "show": true
            }
          ],
          "yaxis": {
            "align": false
          }
        },
        {
          "collapsed": false,
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 26
          },
          "id": 68,
          "panels": [],
          "title": "Extra",
          "type": "row"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "green",
                "mode": "fixed"
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
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 0,
            "y": 27
          },
          "id": 74,
          "options": {
            "minVizHeight": 75,
            "minVizWidth": 75,
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showThresholdLabels": false,
            "showThresholdMarkers": true,
            "sizing": "auto"
          },
          "pluginVersion": "10.3.1",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "exemplar": false,
              "expr": "kyuubi_exec_pool_threads_alive{job=~\"$job\",namespace=~\"$namespace\",instance=~\"$instance\"}",
              "format": "time_series",
              "instant": false,
              "legendFormat": "{{instance}}",
              "range": true,
              "refId": "A"
            }
          ],
          "title": "Alive Thread",
          "type": "gauge"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "green",
                "mode": "fixed"
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
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 12,
            "y": 27
          },
          "id": 73,
          "options": {
            "minVizHeight": 75,
            "minVizWidth": 75,
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showThresholdLabels": false,
            "showThresholdMarkers": true,
            "sizing": "auto"
          },
          "pluginVersion": "10.3.1",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "exemplar": false,
              "expr": "kyuubi_exec_pool_threads_active{job=~\"$job\",namespace=~\"$namespace\",instance=~\"$instance\"}",
              "format": "time_series",
              "instant": false,
              "legendFormat": "{{instance}}",
              "range": true,
              "refId": "A"
            }
          ],
          "title": "Active Thread",
          "type": "gauge"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "green",
                "mode": "fixed"
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
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 0,
            "y": 35
          },
          "id": 41,
          "options": {
            "minVizHeight": 75,
            "minVizWidth": 75,
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showThresholdLabels": false,
            "showThresholdMarkers": true,
            "sizing": "auto"
          },
          "pluginVersion": "10.3.1",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "exemplar": false,
              "expr": "sum(kyuubi_connection_opened{job=~\"$job\",namespace=~\"$namespace\"})",
              "format": "time_series",
              "instant": false,
              "legendFormat": "Connection Opened",
              "range": true,
              "refId": "A"
            },
            {
              "datasource": {
                "type": "prometheus",
                "uid": "${KYUUBI_METRIC_DS}"
              },
              "editorMode": "code",
              "expr": "sum(kyuubi_connection_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Connection Total",
              "range": true,
              "refId": "B"
            }
          ],
          "title": "Connection Statistics",
          "type": "gauge"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "green",
                "mode": "fixed"
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
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 12,
            "y": 35
          },
          "id": 47,
          "options": {
            "minVizHeight": 75,
            "minVizWidth": 75,
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showThresholdLabels": false,
            "showThresholdMarkers": true,
            "sizing": "auto"
          },
          "pluginVersion": "10.3.1",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_operation_state_ExecuteStatement_running_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "legendFormat": "Running",
              "range": true,
              "refId": "A"
            },
            {
              "datasource": {
                "type": "prometheus",
                "uid": "${KYUUBI_METRIC_DS}"
              },
              "editorMode": "code",
              "expr": "sum(kyuubi_operation_state_ExecuteStatement_finished_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Finished",
              "range": true,
              "refId": "B"
            },
            {
              "datasource": {
                "type": "prometheus",
                "uid": "${KYUUBI_METRIC_DS}"
              },
              "editorMode": "code",
              "expr": "sum(kyuubi_operation_state_ExecuteStatement_error_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Error",
              "range": true,
              "refId": "C"
            },
            {
              "datasource": {
                "type": "prometheus",
                "uid": "${KYUUBI_METRIC_DS}"
              },
              "editorMode": "code",
              "expr": "sum(kyuubi_operation_state_ExecuteStatement_pending_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Pending",
              "range": true,
              "refId": "D"
            }
          ],
          "title": "Operation Statistics",
          "type": "gauge"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              },
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 0,
            "y": 43
          },
          "id": 72,
          "options": {
            "minVizHeight": 75,
            "minVizWidth": 75,
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showThresholdLabels": false,
            "showThresholdMarkers": true,
            "sizing": "auto"
          },
          "pluginVersion": "10.3.1",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_engine_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "legendFormat": "{{instance}}",
              "range": true,
              "refId": "A"
            }
          ],
          "title": "Engine Created",
          "type": "gauge"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
          "description": "",
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "green",
                "mode": "fixed"
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
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 12,
            "y": 43
          },
          "id": 75,
          "options": {
            "minVizHeight": 75,
            "minVizWidth": 75,
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showThresholdLabels": false,
            "showThresholdMarkers": true,
            "sizing": "auto"
          },
          "pluginVersion": "10.3.1",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_operation_state_LaunchEngine_running_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "legendFormat": "Running",
              "range": true,
              "refId": "A"
            },
            {
              "datasource": {
                "type": "prometheus",
                "uid": "${KYUUBI_METRIC_DS}"
              },
              "editorMode": "code",
              "expr": "sum(kyuubi_operation_state_LaunchEngine_finished_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Finished",
              "range": true,
              "refId": "B"
            },
            {
              "datasource": {
                "type": "prometheus",
                "uid": "${KYUUBI_METRIC_DS}"
              },
              "editorMode": "code",
              "expr": "sum(kyuubi_operation_state_LaunchEngine_pending_total{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Pending",
              "range": true,
              "refId": "D"
            }
          ],
          "title": "Launch Engine Statistics",
          "type": "gauge"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "green",
                "mode": "fixed"
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
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 0,
            "y": 51
          },
          "id": 45,
          "options": {
            "minVizHeight": 75,
            "minVizWidth": 75,
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showThresholdLabels": false,
            "showThresholdMarkers": true,
            "sizing": "auto"
          },
          "pluginVersion": "10.3.1",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_rest_connection_opened{job=~\"$job\",namespace=~\"$namespace\"})",
              "legendFormat": "{{instance}}",
              "range": true,
              "refId": "A"
            }
          ],
          "title": "Rest Connection Opened",
          "type": "gauge"
        },
        {
          "datasource": {
            "type": "prometheus",
            "uid": "${KYUUBI_METRIC_DS}"
          },
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
                  "mode": "line"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              },
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 12,
            "x": 12,
            "y": 51
          },
          "id": 49,
          "options": {
            "legend": {
              "calcs": [],
              "displayMode": "list",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "single",
              "sort": "none"
            }
          },
          "pluginVersion": "9.1.6",
          "targets": [
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_thread_state_runnable_count)",
              "legendFormat": "Runnable",
              "range": true,
              "refId": "A"
            },
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_thread_state_blocked_count{job=~\"$job\",namespace=~\"$namespace\"})\r\n",
              "hide": false,
              "legendFormat": "Blocked",
              "range": true,
              "refId": "B"
            },
            {
              "datasource": "${KYUUBI_METRIC_DS}",
              "editorMode": "code",
              "expr": "sum(kyuubi_thread_state_waiting_count{job=~\"$job\",namespace=~\"$namespace\"})",
              "hide": false,
              "legendFormat": "Waiting",
              "range": true,
              "refId": "C"
            }
          ],
          "title": "Thread Status",
          "type": "timeseries"
        }
      ],
      "refresh": "30s",
      "revision": 1,
      "schemaVersion": 39,
      "tags": [],
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
            "label": "datasource",
            "multi": false,
            "name": "KYUUBI_METRIC_DS",
            "options": [],
            "query": "prometheus",
            "queryValue": "",
            "refresh": 1,
            "regex": "",
            "skipUrlSync": false,
            "type": "datasource"
          },
          {
            "datasource": {
              "type": "prometheus",
              "uid": "${KYUUBI_METRIC_DS}"
            },
            "definition": "",
            "hide": 2,
            "includeAll": true,
            "label": "Job",
            "multi": true,
            "name": "job",
            "options": [],
            "query": "label_values(kyuubi_memory_usage_total_init, job)",
            "refresh": 2,
            "regex": "",
            "skipUrlSync": false,
            "sort": 1,
            "type": "query"
          },
          {
            "allValue": ".*",
            "current": {
              "selected": false,
              "text": "${namespace}",
              "value": "${namespace}"
            },
            "datasource": {
              "uid": "${KYUUBI_METRIC_DS}"
            },
            "definition": "",
            "hide": 0,
            "includeAll": true,
            "label": "Namespace",
            "multi": true,
            "name": "namespace",
            "options": [],
            "query": "label_values(kyuubi_memory_usage_total_init, namespace)",
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
              "uid": "${KYUUBI_METRIC_DS}"
            },
            "definition": "label_values(kyuubi_memory_usage_total_init,instance)",
            "hide": 0,
            "includeAll": true,
            "label": "instance",
            "multi": true,
            "name": "instance",
            "options": [],
            "query": {
              "query": "label_values(kyuubi_memory_usage_total_init,instance)",
              "refId": "StandardVariableQuery"
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
        "from": "now-6h",
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
      "title": "Kyuubi",
      "uid": "kyuubi001",
      "version": 4,
      "weekStart": ""
    }
    </#noparse>