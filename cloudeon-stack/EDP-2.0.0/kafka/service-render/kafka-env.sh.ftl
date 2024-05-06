export LOG_DIR="/workspace/logs"
<#assign heapRamPercentage = conf['kafka.server.heap.memory.percentage']?number>
<#assign directRamPercentage = conf['kafka.server.direct.memory.percentage']?number>
export heapRam=$[ $MEM_LIMIT / 1024 / 1024  * ${heapRamPercentage} / 100 ]M
export directRam=$[ $MEM_LIMIT / 1024 / 1024  * ${directRamPercentage} / 100 ]M

# 环境变量由kafka-run-class.sh注入
export JMX_PORT=${conf['kafka.jmx.port']}
export KAFKA_HEAP_OPTS="-Xmx$heapRam -Xms$heapRam -XX:MaxDirectMemorySize=$directRam"

export KAFKA_OPTS="$KAFKA_OPTS -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent.jar=5551:$KAFKA_HOME/config/jmx_prometheus.yaml"
export KAFKA_OPTS="$KAFKA_OPTS -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/workspace/logs/gc-kafka-broker.log"
