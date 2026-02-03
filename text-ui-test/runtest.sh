#!/usr/bin/env bash

pushd "$(dirname "$0")" >/dev/null

if [ -d "../bin" ]
then
    rm -rf ../bin
fi
mkdir ../bin

# delete output from previous run
if [ -e "./ACTUAL.TXT" ]
then
    rm ACTUAL.TXT
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
    popd >/dev/null
    exit 1
fi
rm sources.txt

cd ..

# run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath bin tobtahc.Main < "$(dirname "$0")"/input.txt > "$(dirname "$0")"/ACTUAL.TXT

cd "$(dirname "$0")"

# convert to UNIX format
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT

# compare the output to the expected output
diff ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi

popd >/dev/null
