#!/usr/bin/env sh
APP_HOME=$(dirname "$0")
APP_HOME=$(cd "$APP_HOME" && pwd)
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
JAVA_EXE=java
exec "$JAVA_EXE" -Xms64m -Xmx512m -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
