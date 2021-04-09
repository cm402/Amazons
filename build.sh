#!/bin/sh

THE_CLASSPATH=
PROGRAM_NAME=GameEngine
cd src
for i in `ls ../lib/*.jar`
    do
    THE_CLASSPATH=${THE_CLASSPATH}:${i}
done

java -cp ".:${THE_CLASSPATH}" $PROGRAM_NAME $1

if [ $? -eq 0 ]
then
    echo "compile successful!"
fi