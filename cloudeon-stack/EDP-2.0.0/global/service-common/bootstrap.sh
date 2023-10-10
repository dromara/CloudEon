#!/bin/bash
set -e

basepath=$(cd `dirname $0`; pwd)
b_array=$(find $basepath -mindepth 2  -maxdepth 2 -name "bootstrap.sh")
b_array=$(echo ${b_array[*]} | tr ' ' '\n' | sort -n)
echo '${b_array[@]}:'${b_array[@]}
for b in ${b_array[@]} ; do
  echo "begin execute "$b
  bash $b
  echo "end execute "$b
done