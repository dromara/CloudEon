#!/bin/bash

source  /opt/edp/${service.serviceName}/conf/install-iceberg.sh

${r"${KYUUBI_HOME}"}/bin/kyuubi start


tail -f /dev/null