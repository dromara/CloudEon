#!/usr/bin/env bash
set -o errexit
set -o errtrace
set -o nounset
set -o pipefail
set -o xtrace
source ${r"$HADOOP_CONF_DIR"}/hadoop-hdfs-env.sh

echo "========================starting journalnode========================"
${r"${HADOOP_HOME}"}/bin/hdfs --config /opt/udh/${service.serviceName}/conf journalnode

