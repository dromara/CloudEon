#!/bin/bash
set +e

export HIVE_CONF_DIR=$HIVE_HOME/conf
/bin/bash  -c "schematool -dbType mysql -initSchema -verbose"