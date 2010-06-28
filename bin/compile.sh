#!/bin/bash

cdir=`pwd`
JAVA_HOME=/usr/java/j2sdk
srcdir=$cdir/src
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

echo $CLASSPATH

jflist=`find $srcdir -name "*.java" -print`
for jf in $jflist
do
  cf=`echo $jf | sed s/\.java$/\.class/ | sed s/src/classes/`

  if [ -f $cf -a $cf -nt $jf ]; then
     echo "$cf0 is up to date" > /dev/null
     # echo "$cf0 is up to date"
  else
     echo "javac $jf to $cdir/classes..."
     $JAVA_HOME/bin/javac -d $classdir $jf
  fi
done
