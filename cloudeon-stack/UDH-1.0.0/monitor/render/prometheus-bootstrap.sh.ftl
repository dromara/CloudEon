#!/usr/bin/env bash



export LOG_DIR=/opt/udh/${service.serviceName}/log
export PID_DIR=/opt/udh/${service.serviceName}/prometheus/data

export HOSTNAME=`hostname`

log=$LOG_DIR/prometheus-$HOSTNAME.out
pid=$PID_DIR/prometheus.pid

echo "========================start prometheus========================"

exec_command="prometheus --config.file=/opt/udh/${service.serviceName}/conf/prometheus.yml --storage.tsdb.path="/opt/udh/${service.serviceName}/prometheus/data"  --web.listen-address=:${conf['prometheus.http.port']} --web.enable-lifecycle"
echo "nohup $exec_command > $log 2>&1 &"
nohup $exec_command > $log 2>&1 &
echo $! > $pid

tail -f /dev/null