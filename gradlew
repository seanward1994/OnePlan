#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS:-} -Dfile.encoding=UTF-8" \
exec "${DIR}/gradle/wrapper/gradle-wrapper.jar" 2>/dev/null || \
exec "${DIR}/gradle" 2>/dev/null || \
exec ./gradle -q "$@"
