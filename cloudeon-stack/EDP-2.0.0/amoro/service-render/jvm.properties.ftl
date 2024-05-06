<#assign ramPercentage = conf['amoro.ams.jvm.memory.percentage']?number>
<#assign ramMb = conf['amoro.ams.container.limit.memory']?number>
<#assign serverHeap = (ramMb * ramPercentage /100)?floor?c>

xms=${serverHeap}
xmx=${serverHeap}
jmx.remote.port=9928
extra.options="-XX:NewRatio=1 -XX:SurvivorRatio=3 -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent.jar=5559:/opt/amoro/conf/jmx_prometheus.yaml "
