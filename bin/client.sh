#!/bin/bash

#cdir=`pwd`
#java -cp $cdir/classes org.mycoz.Mycoz
cdir=`pwd`
JAVA_HOME=/usr/java/j2sdk
#srcdir=$cdir/src
classdir=$cdir/classes
libdir=$cdir/lib

export CLASSPATH=.:$classdir
if [ -e $libdir ]; then
	libfilelist=`find $libdir -name "*.jar" -print`
	for lf in $libfilelist
	do
  		export CLASSPATH=$CLASSPATH:$lf
	done
fi
#echo $CLASSPATH

echo "$JAVA_HOME/bin/java -cp $CLASSPATH java java com.mooo.mycoz.sockt.Client"
$JAVA_HOME/bin/java -cp $CLASSPATH com.mooo.mycoz.sockt.Client


