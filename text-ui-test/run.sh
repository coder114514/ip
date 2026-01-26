#!/usr/bin/env bash

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# compile the code into the bin folder, terminates if error occurred
if [ -e "./sources.txt" ]
then
    rm sources.txt
fi
find ../src/main/java -name "*.java" > sources.txt
if ! javac -cp ../src/main/java -Xlint:none -d ../bin @sources.txt
then
    echo "********** BUILD FAILURE **********"
    rm sources.txt
    exit 1
fi
rm sources.txt

# run the program
java -classpath ../bin tobtahc.TobTahc
