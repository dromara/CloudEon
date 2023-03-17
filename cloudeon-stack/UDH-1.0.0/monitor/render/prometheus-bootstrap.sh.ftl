#!/usr/bin/env bash



export LOG_DIR=$SH_DIR/logs
export PID_DIR=$SH_DIR/pid

export HOSTNAME=`hostname`

log=$LOG_DIR/$command-$HOSTNAME.out
pid=$PID_DIR/$command.pid

echo "========================start prometheus========================"

exec_command="prometheus --config.file=/opt/udh/${service.serviceName}/conf/prometheus.yml --web.listen-address=:9090 --web.enable-lifecycle"
echo "nohup $exec_command > $log 2>&1 &"
nohup $exec_command > $log 2>&1 &
echo $! > $pid

tail -f /dev/null