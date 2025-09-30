#!/usr/bin/env bash
set -euo pipefail
TS="$(date -u +'%Y-%m-%dT%H:%M:%SZ')"
OUT="FULL_CODE_DUMP.txt"
echo "# OnePlan — full source dump ($TS)" > "$OUT"
echo >> "$OUT"
echo "## File list" >> "$OUT"
git ls-files | sort >> "$OUT"
echo >> "$OUT"
echo "## Sources" >> "$OUT"
git ls-files ':!**/*.png' ':!**/*.jpg' ':!**/*.jpeg' ':!**/*.webp' ':!**/*.gif' ':!**/*.jar' |
  while read -r f; do
    echo -e "\n\n-----8<----- FILE: $f -----8<-----\n" >> "$OUT"
    sed -n '1,4000p' "$f" >> "$OUT" || true
  done
echo "✅ Wrote $OUT"
