@echo off
cd ..

set PROPJECT_HOME=%cd%
set LIB_HOME=%PROPJECT_HOME%\lib
set SRC_HOME=%PROPJECT_HOME%\src
set CLASSES_HOME=%PROPJECT_HOME%\classes

set CLASSPATH=.;%CLASSES_HOME%

IF EXIST %PROPJECT_HOME%\lib for /r %PROPJECT_HOME%\lib %%i in (*.jar) do call set CLASSPATH=%%CLASSPATH%%;%%i

for /r %PROPJECT_HOME%\src %%i in (*.java) do (
@echo javac %%i to %CLASSES_HOME%
javac -encoding UTF-8 -d %CLASSES_HOME% %%i
)
cd bin

@echo on
@pause