#!/usr/bin/env bash



export LOG_DIR=/opt/udh/${service.serviceName}/log
export PID_DIR=/opt/udh/${service.serviceName}/alertmanager/data

export HOSTNAME=`hostname`

log=$LOG_DIR/alertmanager-$HOSTNAME.out
pid=$PID_DIR/alertmanager.pid

echo "========================start alertmanager========================"

exec_command="alertmanager --config.file=/opt/udh/${service.serviceName}/conf/alertmanager.yml --storage.path="/opt/udh/${service.serviceName}/alertmanager/data" --cluster.advertise-address=0.0.0.0:${conf['alertmanager.http.port']} "
echo "nohup $exec_command > $log 2>&1 &"
nohup $exec_command > $log 2>&1 &
echo $! > $pid

tail -f /dev/null