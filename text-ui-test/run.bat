@ECHO OFF

pushd %~dp0

if exist ..\bin rmdir /S /Q ..\bin
mkdir ..\bin

REM compile the code into the bin folder
if exist sources.txt del sources.txt
dir /S /B ..\src\main\java\*.java > sources.txt
javac -cp ..\src\main\java -Xlint:none -d ..\bin @sources.txt
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    del sources.txt
    popd
    exit /b 1
)
REM no error here, errorlevel == 0
del sources.txt

cd ..

REM run the program
java -classpath bin tobtahc.Main

popd
