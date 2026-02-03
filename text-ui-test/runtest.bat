@ECHO OFF

pushd %~dp0

if exist ..\bin rmdir /S /Q ..\bin
mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

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

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath bin tobtahc.Main < %~dp0\input.txt > %~dp0\ACTUAL.TXT

cd %~dp0

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT

popd
