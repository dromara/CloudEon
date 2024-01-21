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
      "description": "ElasticSearch cluster stats",
      "editable": true,
      "fiscalYearStartMonth": 0,
      "gnetId": 2322,
      "graphTooltip": 1,
      "id": 73,
      "links": [
        {
          "asDropdown": true,
          "icon": "external link",
          "includeVars": false,
          "keepTime": true,
          "tags": [
            "OS"
          ],
          "targetBlank": true,
          "title": "OS",
          "type": "dashboards"
        },
        {
          "asDropdown": true,
          "icon": "external link",
          "keepTime": true,
          "tags": [
            "MySQL"
          ],
          "targetBlank": true,
          "title": "MySQL",
          "type": "dashboards"
        },
        {
          "asDropdown": true,
          "icon": "external link",
          "keepTime": true,
          "tags": [
            "MongoDB"
          ],
          "targetBlank": true,
          "title": "MongoDB",
          "type": "dashboards"
        },
        {
          "asDropdown": true,
          "icon": "external link",
          "keepTime": true,
          "tags": [
            "App"
          ],
          "targetBlank": true,
          "title": "App",
          "type": "dashboards"
        }
      ],
      "liveNow": false,
      "panels": [
        {
          "collapsed": false,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 0
          },
          "id": 90,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "KPI",
          "type": "row"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
              },
              "mappings": [
                {
                  "options": {
                    "1": {
                      "text": "Red"
                    },
                    "3": {
                      "text": "Yellow"
                    },
                    "5": {
                      "text": "Green"
                    }
                  },
                  "type": "value"
                },
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
                    "color": "#d44a3a",
                    "value": null
                  },
                  {
                    "color": "rgba(237, 129, 40, 0.89)",
                    "value": 2
                  },
                  {
                    "color": "#299c46",
                    "value": 4
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 0,
            "y": 1
          },
          "id": 53,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "value",
            "graphMode": "area",
            "justifyMode": "auto",
            "orientation": "horizontal",
            "reduceOptions": {
              "calcs": [
                "mean"
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
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_status{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",color=\"red\"}==1 or (elasticsearch_cluster_health_status{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",color=\"green\"}==1)+4 or (elasticsearch_cluster_health_status{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",color=\"yellow\"}==1)+22",
              "format": "time_series",
              "instant": true,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A"
            }
          ],
          "title": "Cluster health",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
              },
              "mappings": [
                {
                  "options": {
                    "N/A": {
                      "color": "#299c46",
                      "text": "0"
                    },
                    "no value": {
                      "color": "#299c46",
                      "text": "0"
                    }
                  },
                  "type": "value"
                },
                {
                  "options": {
                    "match": "null",
                    "result": {
                      "color": "#299c46",
                      "text": "0"
                    }
                  },
                  "type": "special"
                }
              ],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "#299c46",
                    "value": null
                  },
                  {
                    "color": "rgba(237, 129, 40, 0.89)",
                    "value": 1
                  },
                  {
                    "color": "#d44a3a",
                    "value": 2
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 2,
            "x": 4,
            "y": 1
          },
          "id": 81,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "value",
            "graphMode": "area",
            "justifyMode": "auto",
            "orientation": "horizontal",
            "reduceOptions": {
              "calcs": [
                "mean"
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
                "uid": "$datasource"
              },
              "expr": "count(elasticsearch_breakers_tripped{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}>0)",
              "format": "time_series",
              "instant": true,
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A"
            }
          ],
          "title": "Tripped for breakers",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
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
                    "color": "rgba(50, 172, 45, 0.97)",
                    "value": null
                  },
                  {
                    "color": "rgba(237, 129, 40, 0.89)",
                    "value": 70
                  },
                  {
                    "color": "rgba(245, 54, 54, 0.9)",
                    "value": 80
                  }
                ]
              },
              "unit": "percent"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 6,
            "y": 1
          },
          "id": 51,
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "sum (elasticsearch_process_cpu_percent{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"} ) / count (elasticsearch_process_cpu_percent{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"} )",
              "format": "time_series",
              "instant": true,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "",
              "metric": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "CPU usage Avg.",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
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
                    "color": "rgba(50, 172, 45, 0.97)",
                    "value": null
                  },
                  {
                    "color": "rgba(237, 129, 40, 0.89)",
                    "value": 70
                  },
                  {
                    "color": "rgba(245, 54, 54, 0.9)",
                    "value": 80
                  }
                ]
              },
              "unit": "percent"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 10,
            "y": 1
          },
          "id": 50,
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "sum (elasticsearch_jvm_memory_used_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}) / sum (elasticsearch_jvm_memory_max_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}) * 100",
              "format": "time_series",
              "instant": true,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "",
              "metric": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "JVM memory used Avg.",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "Number of nodes in the cluster",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
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
                    "value": 80
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 2,
            "x": 14,
            "y": 1
          },
          "id": 10,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_number_of_nodes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"}",
              "format": "time_series",
              "instant": true,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "",
              "metric": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Nodes",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "Number of data nodes in the cluster",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
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
                    "value": 80
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 2,
            "x": 16,
            "y": 1
          },
          "id": 9,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_number_of_data_nodes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"}",
              "format": "time_series",
              "instant": true,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "",
              "metric": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Data nodes",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "Cluster level changes which have not yet been executed",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
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
                    "color": "rgba(50, 172, 45, 0.97)",
                    "value": null
                  },
                  {
                    "color": "rgba(237, 129, 40, 0.89)",
                    "value": 1
                  },
                  {
                    "color": "rgba(245, 54, 54, 0.9)",
                    "value": 5
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 2,
            "x": 18,
            "y": 1
          },
          "hideTimeOverride": true,
          "id": 16,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "value",
            "graphMode": "area",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_number_of_pending_tasks{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"}",
              "format": "time_series",
              "instant": true,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "",
              "metric": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Pending tasks",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
              "unit": "short"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 20,
            "y": 1
          },
          "id": 89,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "sum (elasticsearch_process_open_files_count{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"})",
              "format": "time_series",
              "instant": true,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "",
              "metric": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Open file descriptors per cluster",
          "type": "stat"
        },
        {
          "collapsed": false,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 4
          },
          "id": 91,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Shards",
          "type": "row"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "The number of primary shards in your cluster. This is an aggregate total across all indices.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "rgb(31, 120, 193)",
                "mode": "fixed"
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
                    "value": 80
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 0,
            "y": 5
          },
          "id": 11,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
            "graphMode": "area",
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
          "pluginVersion": "10.2.3",
          "repeat": "shard_type",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_active_primary_shards{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"}",
              "format": "time_series",
              "instant": true,
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Active primary shards",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "Aggregate total of all shards across all indices, which includes replica shards",
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "rgb(31, 120, 193)",
                "mode": "fixed"
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
                    "value": 80
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 4,
            "y": 5
          },
          "id": 39,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
            "graphMode": "area",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_active_shards{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"}",
              "format": "time_series",
              "instant": true,
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Active shards",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "Count of shards that are being freshly created",
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "rgb(31, 120, 193)",
                "mode": "fixed"
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
                    "value": 80
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 8,
            "y": 5
          },
          "id": 40,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
            "graphMode": "area",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_initializing_shards{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"}",
              "format": "time_series",
              "instant": true,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Initializing shards",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "The number of shards that are currently moving from one node to another node.",
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "rgb(31, 120, 193)",
                "mode": "fixed"
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
                    "value": 80
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 12,
            "y": 5
          },
          "id": 41,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
            "graphMode": "area",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_relocating_shards{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"}",
              "format": "time_series",
              "instant": true,
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Relocating shards",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "Shards delayed to reduce reallocation overhead",
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "rgb(31, 120, 193)",
                "mode": "fixed"
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
                    "value": 80
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 16,
            "y": 5
          },
          "id": 42,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
            "graphMode": "area",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_delayed_unassigned_shards{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"} ",
              "format": "time_series",
              "instant": true,
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Delayed shards",
          "type": "stat"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "The number of shards that exist in the cluster state, but cannot be found in the cluster itself",
          "fieldConfig": {
            "defaults": {
              "color": {
                "fixedColor": "rgb(31, 120, 193)",
                "mode": "fixed"
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
                    "value": 80
                  }
                ]
              },
              "unit": "none"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 4,
            "x": 20,
            "y": 5
          },
          "id": 82,
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "none",
            "graphMode": "area",
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
          "pluginVersion": "10.2.3",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_cluster_health_unassigned_shards{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\"} ",
              "format": "time_series",
              "instant": true,
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A",
              "step": 60
            }
          ],
          "title": "Unassigned shards",
          "type": "stat"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 8
          },
          "id": 92,
          "panels": [
            {
              "datasource": {
                "uid": "$datasource"
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
                    "axisLabel": "GCs",
                    "axisPlacement": "auto",
                    "barAlignment": 0,
                    "drawStyle": "line",
                    "fillOpacity": 10,
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
                    "showPoints": "never",
                    "spanNulls": true,
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
                        "color": "green"
                      },
                      {
                        "color": "red",
                        "value": 80
                      }
                    ]
                  },
                  "unit": "short",
                  "unitScale": true
                },
                "overrides": []
              },
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 0,
                "y": 9
              },
              "id": 7,
              "links": [],
              "options": {
                "legend": {
                  "calcs": [
                    "mean",
                    "lastNotNull",
                    "max",
                    "min"
                  ],
                  "displayMode": "table",
                  "placement": "bottom",
                  "showLegend": true
                },
                "tooltip": {
                  "mode": "multi",
                  "sort": "desc"
                }
              },
              "pluginVersion": "7.3.6",
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_jvm_gc_collection_seconds_count{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}} - {{gc}}",
                  "metric": "",
                  "refId": "A",
                  "step": 10
                }
              ],
              "title": "GC count",
              "type": "timeseries"
            },
            {
              "datasource": {
                "uid": "$datasource"
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
                    "axisLabel": "Time",
                    "axisPlacement": "auto",
                    "barAlignment": 0,
                    "drawStyle": "line",
                    "fillOpacity": 10,
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
                    "showPoints": "never",
                    "spanNulls": true,
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
                        "color": "green"
                      },
                      {
                        "color": "red",
                        "value": 80
                      }
                    ]
                  },
                  "unit": "s",
                  "unitScale": true
                },
                "overrides": []
              },
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 12,
                "y": 9
              },
              "id": 27,
              "links": [],
              "options": {
                "legend": {
                  "calcs": [
                    "mean",
                    "lastNotNull",
                    "max",
                    "min"
                  ],
                  "displayMode": "table",
                  "placement": "bottom",
                  "showLegend": true
                },
                "tooltip": {
                  "mode": "multi",
                  "sort": "desc"
                }
              },
              "pluginVersion": "7.3.6",
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_jvm_gc_collection_seconds_sum{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}} - {{gc}}",
                  "metric": "",
                  "refId": "A",
                  "step": 10
                }
              ],
              "title": "GC time",
              "type": "timeseries"
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "JVM Garbage Collection",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 9
          },
          "id": 93,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 12,
                "x": 0,
                "y": 10
              },
              "hiddenSeries": false,
              "id": 77,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "total": true,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_translog_operations{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Total translog operations",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 12,
                "x": 12,
                "y": 10
              },
              "hiddenSeries": false,
              "id": 78,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_translog_size_in_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Total translog size in bytes",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Translog",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 10
          },
          "id": 94,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 11
              },
              "hiddenSeries": false,
              "id": 79,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_breakers_tripped{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: {{breaker}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Tripped for breakers",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 18
              },
              "hiddenSeries": false,
              "id": 80,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_breakers_estimated_size_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: {{breaker}}",
                  "refId": "A"
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_breakers_limit_size_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: limit for {{breaker}}",
                  "refId": "B"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Estimated size in bytes of breaker",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Breakers",
          "type": "row"
        },
        {
          "collapsed": false,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 11
          },
          "id": 95,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "CPU and Memory",
          "type": "row"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
                "axisLabel": "CPU usage",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 10,
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
                "showPoints": "never",
                "spanNulls": true,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "max": 100,
              "min": 0,
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
              "unit": "short"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 12,
            "x": 0,
            "y": 12
          },
          "id": 30,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_os_load1{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "instant": false,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "load1: {{name}}",
              "metric": "",
              "refId": "A",
              "step": 20
            },
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_os_load5{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "instant": false,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "load5: {{name}}",
              "metric": "",
              "refId": "B",
              "step": 20
            },
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_os_load15{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "instant": false,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "load15: {{name}}",
              "metric": "",
              "refId": "C",
              "step": 20
            }
          ],
          "title": "Load average",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
                "axisLabel": "CPU usage",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 10,
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
                "showPoints": "never",
                "spanNulls": true,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "max": 100,
              "min": 0,
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
              "unit": "percent"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 12,
            "x": 12,
            "y": 12
          },
          "id": 88,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_process_cpu_percent{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "instant": false,
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "{{name}}",
              "metric": "",
              "refId": "A",
              "step": 20
            }
          ],
          "title": "CPU usage",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
                "axisLabel": "Memory",
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
                "showPoints": "never",
                "spanNulls": true,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "min": 0,
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
              "unit": "bytes"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 12,
            "x": 0,
            "y": 23
          },
          "id": 31,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_jvm_memory_used_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "{{name}} used: {{area}}",
              "metric": "",
              "refId": "A",
              "step": 20
            },
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_jvm_memory_max_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{name}} max: {{area}}",
              "refId": "C",
              "step": 20
            },
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_jvm_memory_pool_peak_used_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{name}} peak used pool: {{pool}}",
              "refId": "D",
              "step": 20
            }
          ],
          "title": "JVM memory usage",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
                "axisLabel": "Memory",
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
                "showPoints": "never",
                "spanNulls": true,
                "stacking": {
                  "group": "A",
                  "mode": "none"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "min": 0,
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
              "unit": "bytes"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 12,
            "x": 12,
            "y": 23
          },
          "id": 54,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "desc"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_jvm_memory_committed_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{name}} committed: {{area}}",
              "refId": "B",
              "step": 20
            },
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_jvm_memory_max_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{name}} max: {{area}}",
              "refId": "C",
              "step": 20
            }
          ],
          "title": "JVM memory committed",
          "type": "timeseries"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 34
          },
          "id": 96,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 0,
                "y": 7
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 32,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": false,
                "show": true,
                "sort": "current",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "1-(elasticsearch_filesystem_data_available_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}/elasticsearch_filesystem_data_size_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"})",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: {{path}}",
                  "metric": "",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [
                {
                  "colorMode": "custom",
                  "fill": true,
                  "fillColor": "rgba(216, 200, 27, 0.27)",
                  "op": "gt",
                  "value": 0.8
                },
                {
                  "colorMode": "custom",
                  "fill": true,
                  "fillColor": "rgba(234, 112, 112, 0.22)",
                  "op": "gt",
                  "value": 0.9
                }
              ],
              "timeRegions": [],
              "title": "Disk usage",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "percentunit",
                  "label": "Disk Usage %",
                  "logBase": 1,
                  "max": 1,
                  "min": 0,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 12,
                "y": 7
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 47,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": false,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [
                {
                  "alias": "sent",
                  "transform": "negative-Y"
                }
              ],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_transport_tx_size_bytes_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: sent ",
                  "refId": "D",
                  "step": 20
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "-irate(elasticsearch_transport_rx_size_bytes_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: received",
                  "refId": "C",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Network usage",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "Bps",
                  "label": "Bytes/sec",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "pps",
                  "label": "",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Disk and Network",
          "type": "row"
        },
        {
          "collapsed": false,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 35
          },
          "id": 97,
          "panels": [],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Documents",
          "type": "row"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
                "fillOpacity": 10,
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
                "showPoints": "never",
                "spanNulls": true,
                "stacking": {
                  "group": "A",
                  "mode": "normal"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "decimals": 2,
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
              "unit": "short"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 12,
            "x": 0,
            "y": 36
          },
          "id": 1,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "none"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "elasticsearch_indices_docs{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
              "format": "time_series",
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "{{name}}",
              "metric": "",
              "refId": "A",
              "step": 20
            }
          ],
          "title": "Documents count on node",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
                "axisLabel": "index calls/s",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 10,
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
                "showPoints": "never",
                "spanNulls": true,
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
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              },
              "unit": "short"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 12,
            "x": 12,
            "y": 36
          },
          "id": 24,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "none"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "irate(elasticsearch_indices_indexing_index_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
              "format": "time_series",
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "{{name}}",
              "metric": "",
              "refId": "A",
              "step": 20
            }
          ],
          "title": "Documents indexed rate",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "$datasource"
          },
          "description": "Count of deleted documents on this node",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisBorderShow": false,
                "axisCenteredZero": false,
                "axisColorMode": "text",
                "axisLabel": "Documents/s",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 10,
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
                "showPoints": "never",
                "spanNulls": true,
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
                    "color": "green"
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              },
              "unit": "short",
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 8,
            "x": 0,
            "y": 47
          },
          "id": 25,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "none"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "irate(elasticsearch_indices_docs_deleted{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
              "format": "time_series",
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "{{name}}",
              "metric": "",
              "refId": "A",
              "step": 20
            }
          ],
          "title": "Documents deleted rate",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
                "axisLabel": "Documents/s",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 10,
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
                "showPoints": "never",
                "spanNulls": true,
                "stacking": {
                  "group": "A",
                  "mode": "normal"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "decimals": 2,
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green"
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              },
              "unit": "short",
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 8,
            "x": 8,
            "y": 47
          },
          "id": 26,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "none"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "rate(elasticsearch_indices_merges_docs_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
              "format": "time_series",
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "{{name}}",
              "metric": "",
              "refId": "A",
              "step": 20
            }
          ],
          "title": "Documents merged rate",
          "type": "timeseries"
        },
        {
          "datasource": {
            "uid": "$datasource"
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
                "axisLabel": "Bytes/s",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 10,
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
                "showPoints": "never",
                "spanNulls": true,
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
                    "color": "green"
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              },
              "unit": "decbytes",
              "unitScale": true
            },
            "overrides": []
          },
          "gridPos": {
            "h": 11,
            "w": 8,
            "x": 16,
            "y": 47
          },
          "id": 52,
          "links": [],
          "options": {
            "legend": {
              "calcs": [
                "mean",
                "lastNotNull",
                "max",
                "min"
              ],
              "displayMode": "table",
              "placement": "bottom",
              "showLegend": true
            },
            "tooltip": {
              "mode": "multi",
              "sort": "none"
            }
          },
          "pluginVersion": "7.3.7",
          "targets": [
            {
              "datasource": {
                "uid": "$datasource"
              },
              "expr": "irate(elasticsearch_indices_merges_total_size_bytes_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
              "format": "time_series",
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "{{name}}",
              "metric": "",
              "refId": "A",
              "step": 20
            }
          ],
          "title": "Documents merged bytes",
          "type": "timeseries"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 58
          },
          "id": 98,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 0,
                "y": 9
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 33,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_search_query_time_seconds{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval]) ",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 10
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Query time",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 0,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "s",
                  "label": "Time",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 12,
                "y": 9
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 5,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_indexing_index_time_seconds_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 10
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Indexing time",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 0,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "s",
                  "label": "Time",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 0,
                "y": 20
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 3,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_merges_total_time_seconds_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 10
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Merging time",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "s",
                  "label": "Time",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 12,
                "y": 20
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 87,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_store_throttle_time_seconds_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 10
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Throttle time for index store",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "s",
                  "label": "Time",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Times",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 59
          },
          "id": 99,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 24,
                "x": 0,
                "y": 10
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 48,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": true,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": true,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_indexing_index_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: indexing",
                  "metric": "",
                  "refId": "A",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_search_query_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: query",
                  "refId": "B",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_search_fetch_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: fetch",
                  "refId": "C",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_merges_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: merges",
                  "refId": "D",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_refresh_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: refresh",
                  "refId": "E",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_flush_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: flush",
                  "refId": "F",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_get_exists_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: get_exists",
                  "refId": "G",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_get_missing_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: get_missing",
                  "refId": "H",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_get_tota{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: get",
                  "refId": "I",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_indexing_delete_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: indexing_delete",
                  "refId": "J",
                  "step": 10
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Total Operations  rate",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "short",
                  "label": "Operations/s",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 24,
                "x": 0,
                "y": 21
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 49,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": true,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_indexing_index_time_seconds_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: indexing",
                  "metric": "",
                  "refId": "A",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_search_query_time_seconds{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: query",
                  "refId": "B",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_search_fetch_time_seconds{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: fetch",
                  "refId": "C",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_merges_total_time_seconds_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: merges",
                  "refId": "D",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_refresh_time_seconds_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: refresh",
                  "refId": "E",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_flush_time_seconds{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: flush",
                  "refId": "F",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_get_exists_time_seconds{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: get_exists",
                  "refId": "G",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_get_time_seconds{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: get_time",
                  "refId": "H",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_get_missing_time_seconds{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: get_missing",
                  "refId": "I",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_indexing_delete_time_seconds_total{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: indexing_delete",
                  "refId": "J",
                  "step": 10
                },
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_indices_get_time_seconds{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: get",
                  "refId": "K",
                  "step": 10
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Total Operations time",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "s",
                  "label": "Time",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Total Operations stats",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 60
          },
          "id": 100,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 20,
                "w": 6,
                "x": 0,
                "y": 11
              },
              "hiddenSeries": false,
              "id": 45,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "hideZero": true,
                "max": true,
                "min": true,
                "show": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_thread_pool_rejected_count{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: {{ type }}",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Thread Pool operations rejected",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 20,
                "w": 6,
                "x": 6,
                "y": 11
              },
              "hiddenSeries": false,
              "id": 46,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "hideZero": true,
                "max": true,
                "min": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_thread_pool_active_count{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: {{ type }}",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Thread Pool operations queued",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 20,
                "w": 6,
                "x": 12,
                "y": 11
              },
              "height": "",
              "hiddenSeries": false,
              "id": 43,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "hideZero": true,
                "max": true,
                "min": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_thread_pool_active_count{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: {{ type }}",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Thread Pool threads active",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 20,
                "w": 6,
                "x": 18,
                "y": 11
              },
              "hiddenSeries": false,
              "id": 44,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "hideZero": true,
                "max": true,
                "min": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "irate(elasticsearch_thread_pool_completed_count{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}: {{ type }}",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Thread Pool operations completed",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Thread Pool",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 61
          },
          "id": 101,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 0,
                "y": 12
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 4,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": false,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": true,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_fielddata_memory_size_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Field data memory size",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 0,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "bytes",
                  "label": "Memory",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 12,
                "x": 12,
                "y": 12
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 34,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": false,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": true,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_fielddata_evictions{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Field data evictions",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 0,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "short",
                  "label": "Evictions/s",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 8,
                "x": 0,
                "y": 23
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 35,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": false,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": true,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_query_cache_memory_size_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Query cache size",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 0,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "bytes",
                  "label": "Size",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 8,
                "x": 8,
                "y": 23
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 36,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": false,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": true,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_query_cache_evictions{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Query cache evictions",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 0,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "short",
                  "label": "Evictions/s",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "editable": true,
              "error": false,
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "grid": {},
              "gridPos": {
                "h": 11,
                "w": 8,
                "x": 16,
                "y": 23
              },
              "height": "400",
              "hiddenSeries": false,
              "id": 84,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": true,
                "hideEmpty": false,
                "hideZero": false,
                "max": true,
                "min": true,
                "rightSide": false,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "connected",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": true,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "rate(elasticsearch_indices_filter_cache_evictions{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}[$__rate_interval])",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "metric": "",
                  "refId": "A",
                  "step": 20
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Evictions from filter cache",
              "tooltip": {
                "msResolution": false,
                "shared": true,
                "sort": 0,
                "value_type": "cumulative"
              },
              "type": "timeseries",
              "xaxis": {
                "mode": "time",
                "show": true,
                "values": []
              },
              "yaxes": [
                {
                  "format": "short",
                  "label": "Evictions/s",
                  "logBase": 1,
                  "show": true
                },
                {
                  "format": "short",
                  "logBase": 1,
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Caches",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 62
          },
          "id": 102,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 13
              },
              "hiddenSeries": false,
              "id": 85,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": true,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_segments_count{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Count of index segments",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 20
              },
              "hiddenSeries": false,
              "id": 86,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": true,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_segments_memory_bytes{job=~\"$job\",instance=~\"$instance\",namespace=~\"$namespace\",name=~\"$name\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{name}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Current memory size of segments in bytes",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Segments",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 63
          },
          "id": 103,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 14
              },
              "hiddenSeries": false,
              "id": 75,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_docs_primary{job=~\"$job\",instance=~\"$instance\"}",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{index}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Count of documents with only primary shards",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 21
              },
              "hiddenSeries": false,
              "id": 83,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_store_size_bytes_primary{job=~\"$job\",instance=~\"$instance\"}",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{index}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Total size of stored index data in bytes with only primary shards on all nodes",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 28
              },
              "hiddenSeries": false,
              "id": 76,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_store_size_bytes_total{job=~\"$job\",instance=~\"$instance\"}",
                  "format": "time_series",
                  "interval": "",
                  "intervalFactor": 2,
                  "legendFormat": "{{index}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Total size of stored index data in bytes with all shards on all nodes",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Indices: Count of documents and Total size",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 64
          },
          "id": 106,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fill": 1,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 129
              },
              "id": 57,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "percentage": false,
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_segment_doc_values_memory_bytes_primary{job=~\"$job\",instance=~\"$instance\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{index}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "title": "Doc values with only primary shards on all nodes in bytes",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ]
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fill": 1,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 136
              },
              "id": 58,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "percentage": false,
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_segment_doc_values_memory_bytes_total{job=~\"$job\",instance=~\"$instance\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{index}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "title": "Doc values with all shards on all nodes in bytes",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ]
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Indices: Doc values",
          "type": "row"
        },
        {
          "collapsed": true,
          "datasource": {
            "type": "prometheus",
            "uid": "grafanacloud-prom"
          },
          "gridPos": {
            "h": 1,
            "w": 24,
            "x": 0,
            "y": 65
          },
          "id": 107,
          "panels": [
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 16
              },
              "hiddenSeries": false,
              "id": 59,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_segment_fields_memory_bytes_primary{job=~\"$job\",instance=~\"$instance\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{index}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Size of fields with only primary shards on all nodes in bytes",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            },
            {
              "aliasColors": {},
              "autoMigrateFrom": "graph",
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": {
                "uid": "$datasource"
              },
              "fieldConfig": {
                "defaults": {
                  "custom": {}
                },
                "overrides": []
              },
              "fill": 1,
              "fillGradient": 0,
              "gridPos": {
                "h": 7,
                "w": 24,
                "x": 0,
                "y": 23
              },
              "hiddenSeries": false,
              "id": 60,
              "legend": {
                "alignAsTable": true,
                "avg": true,
                "current": false,
                "max": true,
                "min": true,
                "rightSide": true,
                "show": true,
                "sort": "avg",
                "sortDesc": true,
                "total": false,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "options": {
                "alertThreshold": true
              },
              "percentage": false,
              "pluginVersion": "7.3.6",
              "pointradius": 5,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "datasource": {
                    "uid": "$datasource"
                  },
                  "expr": "elasticsearch_indices_segment_fields_memory_bytes_total{job=~\"$job\",instance=~\"$instance\"}",
                  "format": "time_series",
                  "intervalFactor": 2,
                  "legendFormat": "{{index}}",
                  "refId": "A"
                }
              ],
              "thresholds": [],
              "timeRegions": [],
              "title": "Size of fields with all shards on all nodes in bytes",
              "tooltip": {
                "shared": true,
                "sort": 2,
                "value_type": "individual"
              },
              "type": "timeseries",
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
                  "show": false
                }
              ],
              "yaxis": {
                "align": false
              }
            }
          ],
          "targets": [
            {
              "datasource": {
                "type": "prometheus",
                "uid": "grafanacloud-prom"
              },
              "refId": "A"
            }
          ],
          "title": "Indices: Fields",
          "type": "row"
        }
      ],
      "refresh": "30s",
      "schemaVersion": 39,
      "tags": [
        "elasticsearch-integration"
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
            "name": "datasource",
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
              "selected": false,
              "text": "All",
              "value": "$__all"
            },
            "datasource": {
              "uid": "$datasource"
            },
            "definition": "label_values(elasticsearch_cluster_health_status, job)",
            "hide": 2,
            "includeAll": false,
            "label": "job",
            "multi": true,
            "name": "job",
            "options": [],
            "query": "label_values(elasticsearch_cluster_health_status, job)",
            "refresh": 2,
            "regex": "",
            "skipUrlSync": false,
            "sort": 0,
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
              "uid": "$datasource"
            },
            "definition": "label_values(elasticsearch_indices_docs{job=~\"$job\"},namespace)",
            "hide": 0,
            "includeAll": false,
            "label": "namespace",
            "multi": true,
            "name": "namespace",
            "options": [],
            "query": "label_values(elasticsearch_indices_docs{job=~\"$job\"},namespace)",
            "refresh": 2,
            "regex": "",
            "skipUrlSync": false,
            "sort": 1,
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
              "uid": "$datasource"
            },
            "definition": "label_values(elasticsearch_indices_docs{job=~\"$job\", namespace=~\"$namespace\", name!=\"\"},name)",
            "hide": 0,
            "includeAll": true,
            "label": "name",
            "multi": true,
            "name": "name",
            "options": [],
            "query": "label_values(elasticsearch_indices_docs{job=~\"$job\", namespace=~\"$namespace\", name!=\"\"},name)",
            "refresh": 2,
            "regex": "",
            "skipUrlSync": false,
            "sort": 1,
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
              "uid": "$datasource"
            },
            "definition": "label_values(elasticsearch_indices_docs{job=~\"$job\",job=~\"$job\", namespace=~\"$namespace\", name!=\"\"},instance)",
            "hide": 0,
            "includeAll": false,
            "label": "instance",
            "multi": true,
            "name": "instance",
            "options": [],
            "query": "label_values(elasticsearch_indices_docs{job=~\"$job\", namespace=~\"$namespace\", name!=\"\"},instance)",
            "refresh": 2,
            "regex": "",
            "skipUrlSync": false,
            "sort": 1,
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
      "timezone": "browser",
      "title": "ElasticSearch Overview",
      "uid": "elasticsearch001",
      "version": 1,
      "weekStart": ""
    }
    </#noparse>
