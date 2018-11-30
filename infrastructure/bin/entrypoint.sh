#!/usr/bin/env bash

echo "## Start JAVA APP"
echo $JVM_OPTS

exec java $JVM_OPTS -jar *.jar