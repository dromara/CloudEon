#!/bin/bash

#usage ./port_status.sh localhost 80 8080 8088
#parameter 1 is host
#parameters after 1 are list of ports to check
#all ports are ok echo 1, else echo 0


if [ $# -lt 2 ] ; then
  echo "USAGE: $0 host [ports]"
  exit
fi

address=$1
ret=0
shift
i=$@


for i in "$@"; do
  if command -v nc >/dev/null 2>&1; then
 # echo "exists nc"
   #  echo  $address $i
     nc -w 10  $address $i  < /dev/null >/dev/null 2>&1

   #  echo status ======= $?
     if [ $? -eq 0 ] ; then
        ret=$(( $ret + 0 ))
     else
        ret=$(( $ret + 1 ))
     fi
  else
    ret=2
  fi
done

echo '结果'$ret
exit $ret