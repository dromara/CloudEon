#!/bin/bash
echo stat|nc localhost 2181 | grep Mode| awk -F ': ' '{print $2}'