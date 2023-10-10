#!/bin/bash

LATEST_PAGE="https://www.oracle.com/java/technologies/downloads/#java8"
DOWNFILE=index.html

# Get file list
wget $LATEST_PAGE

LATEST_VERSION=`grep 'Java SE Development Kit 8' $DOWNFILE | head -1 | sed 's/.*\(8u.*\)<.*/\1/'`
LATEST_PATCH=`echo $LATEST_VERSION | sed 's/8u//g'`
LATEST_URL=`grep data-file= $DOWNFILE | grep -E "jdk-$LATEST_VERSION-linux-x64.tar.gz" | sed "s/.*data-file='\(.*\)'/\1/"`
rm $DOWNFILE

echo "Downloading....."

FILE=jdk-${LATEST_VERSION}-linux-x64.tar.gz
VERSION=`echo $LATEST_URL | awk -F\/ '{print $7}' | sed 's/8u//g'`
HASH=`echo $LATEST_URL | awk -F\/ '{print $8}'`
URL="https://javadl.oracle.com/webapps/download/GetFile/1.8.0_$VERSION/$HASH/linux-i586/$FILE"
wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" $URL $@