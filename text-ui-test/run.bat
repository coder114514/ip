@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM compile the code into the bin folder
if exist sources.txt del sources.txt
dir /S /B ..\src\main\java\*.java > sources.txt
javac -cp ..\src\main\java -Xlint:none -d ..\bin @sources.txt
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    del sources.txt
    exit /b 1
)
REM no error here, errorlevel == 0
del sources.txt

REM run the program
java -classpath ..\bin tobtahc.TobTahc
