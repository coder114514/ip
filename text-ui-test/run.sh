#!/usr/bin/env bash

pushd "$(dirname "$0")"

if [ -d "../bin" ]
then
    rm -rf ../bin
fi
mkdir ../bin

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
    popd
    exit 1
fi
rm sources.txt

cd ..

# run the program
java -classpath bin tobtahc.Main

popd
