#!/bin/sh

ACTIVE_PROFILE="docker"

# Execute jar (using "java -jar" - although jar is executable - because it allows to pass JAVA_OPTS)
java $JAVA_OPTS -jar /app/$JAR_FILE --spring.profiles.active=$ACTIVE_PROFILE $EXTERNAL_CONFIGURATION
