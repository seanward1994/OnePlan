#!/usr/bin/env bash
set -e
APP_BASE_DIR=$(cd "${0%/*}" && pwd -P)
exec "${APP_BASE_DIR}/gradle/wrapper/gradle-wrapper.jar" >/dev/null 2>&1 || true
exec "${APP_BASE_DIR}/gradle/wrapper/gradle-wrapper.jar" 2>/dev/null || true
# If wrapper jar exists, run wrapper; otherwise fallback to system gradle
if [ -f "${APP_BASE_DIR}/gradle/wrapper/gradle-wrapper.jar" ]; then
  JAVA_EXE="${JAVA_HOME}/bin/java"
  if [ ! -x "$JAVA_EXE" ]; then JAVA_EXE="java"; fi
  exec "$JAVA_EXE" -Xmx64m -cp "${APP_BASE_DIR}/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
else
  echo "Wrapper jar missing — falling back to system gradle" >&2
  exec gradle "$@"
fi
