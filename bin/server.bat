@echo off
cd ..

set PROPJECT_HOME=%cd%
set LIB_HOME=%PROPJECT_HOME%\lib
set SRC_HOME=%PROPJECT_HOME%\src
set CLASSES_HOME=%PROPJECT_HOME%\classes

set CLASSPATH=.;%CLASSES_HOME%

@echo %CLASSES_HOME%

IF EXIST %PROPJECT_HOME%\lib for /r %PROPJECT_HOME%\lib %%i in (*.jar) do call set CLASSPATH=%%CLASSPATH%%;%%i

java com.khnt.socket.Server

cd bin

@echo on
@pause