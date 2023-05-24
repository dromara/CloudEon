#!/bin/bash

touch ${r"${KYLIN_HOME}"}/bin/check-env-bypass

${r"${KYLIN_HOME}"}/bin/kylin.sh start

tail -f /dev/null