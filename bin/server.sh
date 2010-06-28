#!/bin/bash

cdir=`pwd`
JAVA_HOME=/usr/java/j2sdk
classdir=$cdir/classes
libdir=$cdir/lib

export CLASSPATH=.:$classdir

if [ -e $libdir ]; then
	echo $libdir
	libfilelist=`find $libdir -name "*.jar" -print`
	for lf in $libfilelist
	do
  		export CLASSPATH=$CLASSPATH:$lf
	done
fi

echo "$JAVA_HOME/bin/java -cp $CLASSPATH java com.mooo.mycoz.sockt.Server"

$JAVA_HOME/bin/java -cp $CLASSPATH com.mooo.mycoz.sockt.Server
