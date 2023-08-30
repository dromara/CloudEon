#!/bin/bash

rm -f /home/hadoop/amoro/bin/config.sh
ln -s /home/hadoop/amoro/conf/config.sh /home/hadoop/amoro/bin/config.sh

/home/hadoop/amoro/bin/ams.sh start

tail -f /dev/null