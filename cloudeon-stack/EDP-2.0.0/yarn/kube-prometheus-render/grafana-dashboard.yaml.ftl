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
      "id": 34,
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
            "uid": "${prometheus_datasource}"
          },
          "description": "The number of Node Managers by state in the Hadoop ResourceManager.",
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
                  }
                ]
              }
            },
            "overrides": []
          },
          "gridPos": {
            "h": 9,
            "w": 24,
            "x": 0,
            "y": 0
          },
          "id": 2,
          "options": {
            "displayMode": "gradient",
            "maxVizHeight": 300,
            "minVizHeight": 10,
            "minVizWidth": 0,
            "namePlacement": "auto",
            "orientation": "horizontal",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showUnfilled": true,
            "sizing": "auto",
            "valueMode": "color"
          },
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_numactivenms{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"ClusterMetrics\",}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "active",
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_numdecommissionednms{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"ClusterMetrics\",}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "decommissioned",
              "refId": "B"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_numlostnms{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"ClusterMetrics\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "lost",
              "refId": "C"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_numunhealthynms{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"ClusterMetrics\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "healthy",
              "refId": "D"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_numrebootednms{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"ClusterMetrics\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "rebooted",
              "refId": "E"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_numshutdownnms{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"ClusterMetrics\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "shutdown",
              "refId": "F"
            }
          ],
          "title": "Node Managers state",
          "type": "bargauge"
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
            "y": 9
          },
          "id": 3,
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
          "title": "Applications",
          "type": "row"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Number of applications by state for the Hadoop ResourceManager.",
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
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              }
            },
            "overrides": []
          },
          "gridPos": {
            "h": 8,
            "w": 24,
            "x": 0,
            "y": 10
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
              "expr": "hadoop_resourcemanager_appsrunning{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "running",
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_appspending{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "pending",
              "refId": "B"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_appskilled{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "killed",
              "refId": "C"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_appssubmitted{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "submitted",
              "refId": "D"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_appscompleted{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "completed",
              "refId": "E"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_appsfailed{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "failed",
              "refId": "F"
            }
          ],
          "title": "Applications state",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The available memory in the Hadoop ResourceManager.",
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
              "unit": "decmbytes"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 0,
            "y": 18
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
              "expr": "hadoop_resourcemanager_allocatedmb{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "allocated",
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_availablemb{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "available",
              "refId": "B"
            }
          ],
          "title": "Available memory",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The available virtual cores in the Hadoop ResourceManager.",
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
            "y": 18
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
              "expr": "hadoop_resourcemanager_availablevcores{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "available",
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_allocatedvcores{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\", name=\"QueueMetrics\",q0=\"root\", q1=\"default\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "allocated",
              "refId": "B"
            }
          ],
          "title": "Available virtual cores",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Number of applications currently running for the NodeManager.",
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
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 0,
            "y": 24
          },
          "id": 22,
          "options": {
            "colorMode": "value",
            "graphMode": "none",
            "justifyMode": "auto",
            "orientation": "auto",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_nodemanager_applicationsrunning{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Applications running",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Number of containers currently allocated for the NodeManager.",
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
                  }
                ]
              },
              "unit": ""
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 6,
            "y": 24
          },
          "id": 23,
          "options": {
            "colorMode": "value",
            "graphMode": "none",
            "justifyMode": "auto",
            "orientation": "auto",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_nodemanager_allocatedcontainers{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Allocated containers",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Average time taken for the NodeManager to localize necessary resources for a container.",
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
                  }
                ]
              },
              "unit": "ms"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 12,
            "y": 24
          },
          "id": 24,
          "options": {
            "colorMode": "value",
            "graphMode": "none",
            "justifyMode": "auto",
            "orientation": "auto",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_nodemanager_localizationdurationmillisavgtime{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Containers localization duration",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "Average time taken to launch a container on the NodeManager after the necessary resources have been localized.",
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
                  }
                ]
              },
              "unit": "ms"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 6,
            "x": 18,
            "y": 24
          },
          "id": 25,
          "options": {
            "colorMode": "value",
            "graphMode": "none",
            "justifyMode": "auto",
            "orientation": "auto",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_nodemanager_containerlaunchdurationavgtime{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Containers launch duration",
          "type": "stat"
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
            "y": 30
          },
          "id": 31,
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
          "title": "Node",
          "type": "row"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The Heap and non-heap memory used for the NodeManager.",
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
                  "mode": "normal"
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
              "unit": "decmbytes"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 0,
            "y": 31
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
              "expr": "hadoop_nodemanager_memheapusedm{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}} - heap",
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_nodemanager_memnonheapusedm{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}} - nonheap",
              "refId": "B"
            }
          ],
          "title": "Node memory used",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The Heap and non-heap memory committed for the NodeManager.",
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
                  "mode": "normal"
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
              "unit": "decmbytes"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 12,
            "y": 31
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
              "expr": "hadoop_nodemanager_memheapcommittedm{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}} - heap",
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_nodemanager_memnonheapcommittedm{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}} - nonheap",
              "refId": "B"
            }
          ],
          "title": "Node memory committed",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "CPU utilization of the Node.",
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
            "h": 6,
            "w": 12,
            "x": 0,
            "y": 37
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
              "expr": "100 * hadoop_nodemanager_nodecpuutilization{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Node CPU utilization",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "GPU utilization of the Node.",
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
            "h": 6,
            "w": 12,
            "x": 12,
            "y": 37
          },
          "id": 15,
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
              "expr": "100 * hadoop_nodemanager_nodegpuutilization{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Node GPU utilization",
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
            "y": 43
          },
          "id": 7,
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
          "title": "ResourceManager JVM",
          "type": "row"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The Heap and non-heap memory used for the ResourceManager.",
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
                  "mode": "normal"
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
              "unit": "decmbytes"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 0,
            "y": 44
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
          "pluginVersion": "10.0.2-cloud.1.94a6f396",
          "targets": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_memheapusedm{name=\"JvmMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "heap",
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_memnonheapusedm{name=\"JvmMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "nonheap",
              "refId": "B"
            }
          ],
          "title": "Memory used",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The Heap and non-heap memory committed for the ResourceManager.",
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
              "unit": "decmbytes"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 12,
            "y": 44
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
              "expr": "hadoop_resourcemanager_memheapcommittedm{name=\"JvmMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "heap",
              "refId": "A"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "expr": "hadoop_resourcemanager_memnonheapcommittedm{name=\"JvmMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "nonheap",
              "refId": "B"
            }
          ],
          "title": "Memory committed",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The recent increase in the number of garbage collection events for the ResourceManager JVM.",
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
            "y": 50
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
              "expr": "increase(hadoop_resourcemanager_gccount{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:])",
              "format": "time_series",
              "interval": "1m",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Garbage collection count",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "description": "The average duration for each garbage collection operation in the ResourceManager JVM.",
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
              "unit": "ms"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 12,
            "y": 50
          },
          "id": 11,
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
              "expr": "increase(hadoop_resourcemanager_gctimemillis{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:]) / clamp_min(increase(hadoop_resourcemanager_gccount{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:]), 1)",
              "format": "time_series",
              "interval": "1m",
              "intervalFactor": 2,
              "legendFormat": "{{namespace}} - {{instance}}",
              "refId": "A"
            }
          ],
          "title": "Average garbage collection time",
          "type": "timeseries"
        },
        {
          "collapsed": true,
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 56
          },
          "id": 26,
          "panels": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "The Heap and non-heap memory used for the NodeManager.",
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
                      "mode": "normal"
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
                  "unit": "decmbytes"
                },
                "overrides": []
              },
              "gridPos": {
                "h": 6,
                "w": 12,
                "x": 0,
                "y": 11
              },
              "id": 27,
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
                  "expr": "hadoop_nodemanager_memheapusedm{name=\"JvmMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - heap",
                  "refId": "A"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_memnonheapusedm{name=\"JvmMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - nonheap",
                  "refId": "B"
                }
              ],
              "title": "Memory used",
              "type": "timeseries"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "The Heap and non-heap memory committed for the NodeManager.",
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
                      "mode": "normal"
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
                  "unit": "decmbytes"
                },
                "overrides": []
              },
              "gridPos": {
                "h": 6,
                "w": 12,
                "x": 12,
                "y": 11
              },
              "id": 28,
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
                  "expr": "hadoop_nodemanager_memheapcommittedm{name=\"JvmMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - heap",
                  "refId": "A"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_memnonheapcommittedm{name=\"JvmMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - nonheap",
                  "refId": "B"
                }
              ],
              "title": "Memory committed",
              "type": "timeseries"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "The recent increase in the number of garbage collection events for the NodeManager JVM.",
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
                "y": 17
              },
              "id": 29,
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
                  "expr": "increase(hadoop_nodemanager_gccount{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:])",
                  "format": "time_series",
                  "interval": "1m",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}}",
                  "refId": "A"
                }
              ],
              "title": "Garbage collection count",
              "type": "timeseries"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "The average duration for each garbage collection operation in the NodeManager JVM.",
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
                  "unit": "ms"
                },
                "overrides": []
              },
              "gridPos": {
                "h": 6,
                "w": 12,
                "x": 12,
                "y": 17
              },
              "id": 30,
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
                  "expr": "increase(hadoop_nodemanager_gctimemillis{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:]) / clamp_min(increase(hadoop_nodemanager_gccount{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}[$__interval:]), 1)",
                  "format": "time_series",
                  "interval": "1m",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}}",
                  "refId": "A"
                }
              ],
              "title": "Average garbage collection time",
              "type": "timeseries"
            }
          ],
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
          "title": "NodeManager JVM",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "uid": "${prometheus_datasource}"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 57
          },
          "id": 16,
          "panels": [
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "Number of containers with a given state for the NodeManager.",
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
                "y": 14
              },
              "id": 17,
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
                  "expr": "hadoop_nodemanager_containerspaused{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"} > 0",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - paused",
                  "refId": "A"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_containerslaunched{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"} > 0",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - launched",
                  "refId": "B"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_containerscompleted{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"} > 0",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - completed",
                  "refId": "C"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_containersfailed{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"} > 0",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - failed",
                  "refId": "D"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_containerskilled{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"} > 0",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - killed",
                  "refId": "E"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_containersiniting{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"} > 0",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - initing",
                  "refId": "F"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_containersreiniting{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"} > 0",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - reiniting",
                  "refId": "G"
                }
              ],
              "title": "Containers state",
              "type": "timeseries"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "Total memory used by containers for the NodeManager.",
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
                  "unit": "decmbytes"
                },
                "overrides": []
              },
              "gridPos": {
                "h": 6,
                "w": 8,
                "x": 8,
                "y": 14
              },
              "id": 18,
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
                  "expr": "hadoop_nodemanager_containerusedmemgb{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}}",
                  "refId": "A"
                }
              ],
              "title": "Containers used memory",
              "type": "timeseries"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "Total virtual memory used by containers for the NodeManager.",
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
                  "unit": "decmbytes"
                },
                "overrides": []
              },
              "gridPos": {
                "h": 6,
                "w": 8,
                "x": 16,
                "y": 14
              },
              "id": 19,
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
                  "expr": "hadoop_nodemanager_containerusedvmemgb{job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}}",
                  "refId": "A"
                }
              ],
              "title": "Containers used virtual memory",
              "type": "timeseries"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "The memory available and currently allocated for containers by the NodeManager.",
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
                  "unit": "decgbytes"
                },
                "overrides": []
              },
              "gridPos": {
                "h": 6,
                "w": 12,
                "x": 0,
                "y": 20
              },
              "id": 20,
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
                  "expr": "hadoop_nodemanager_availablegb{name=\"NodeManagerMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - available",
                  "refId": "A"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_allocatedgb{name=\"NodeManagerMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - allocated",
                  "refId": "B"
                }
              ],
              "title": "Containers available memory",
              "type": "timeseries"
            },
            {
              "datasource": {
                "uid": "${prometheus_datasource}"
              },
              "description": "The virtual available and currently allocated for containers by the NodeManager.",
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
                "y": 20
              },
              "id": 21,
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
                  "expr": "hadoop_nodemanager_availablevcores{name=\"NodeManagerMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - available",
                  "refId": "A"
                },
                {
                  "datasource": {
                    "uid": "${prometheus_datasource}"
                  },
                  "expr": "hadoop_nodemanager_allocatedvcores{name=\"NodeManagerMetrics\", job=~\"$job\", instance=~\"$instance\", namespace=~\"$namespace\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{namespace}} - {{instance}} - allocated",
                  "refId": "B"
                }
              ],
              "title": "Containers available virtual cores",
              "type": "timeseries"
            }
          ],
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
          "title": "Containers",
          "type": "row"
        }
      ],
      "refresh": "30s",
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
            "refresh": 1,
            "regex": "(?!grafanacloud-usage|grafanacloud-ml-metrics).+",
            "skipUrlSync": false,
            "type": "datasource"
          },
          {
            "allValue": ".+",
            "current": {
              "selected": false,
              "text": "All",
              "value": "$__all"
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
            "query": "label_values(hadoop_resourcemanager_activeapplications,job)",
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
              "selected": false,
              "text": "All",
              "value": "$__all"
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
            "query": "label_values(hadoop_resourcemanager_activeapplications{job=~\"$job\"}, instance)",
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
              "selected": false,
              "text": "All",
              "value": "$__all"
            },
            "datasource": {
              "uid": "${prometheus_datasource}"
            },
            "definition": "",
            "hide": 0,
            "includeAll": true,
            "label": "namespace",
            "multi": true,
            "name": "namespace",
            "options": [],
            "query": "label_values(hadoop_resourcemanager_activeapplications{job=~\"$job\"}, namespace)",
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
        "from": "now-30m",
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
      "title": "Apache Hadoop Yarn overview",
      "uid": "yarn001",
      "version": 1,
      "weekStart": ""
    }
    </#noparse>
