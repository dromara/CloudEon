#!/usr/bin/env bash



export LOG_DIR=/opt/udh/${service.serviceName}/log
export PID_DIR=/opt/udh/${service.serviceName}/data/grafana

export HOSTNAME=`hostname`

log=$LOG_DIR/grafana-$HOSTNAME.out
pid=$PID_DIR/grafana.pid

echo "========================start grafana========================"

exec_command="grafana-server --config=/opt/udh/${service.serviceName}/conf/grafana.ini --homepath=/opt/grafana-latest/"
echo "nohup $exec_command > $log 2>&1 &"
nohup $exec_command > $log 2>&1 &
echo $! > $pid

tail -f /dev/null