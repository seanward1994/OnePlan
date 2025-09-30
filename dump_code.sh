#!/usr/bin/env bash
set -euo pipefail
OUT_TXT="FULL_CODE_DUMP.txt"
echo "# OnePlan — full source dump ($(date -u +%Y-%m-%dT%H:%M:%SZ))" > "$OUT_TXT"
git ls-files | sort >> "$OUT_TXT"
echo >> "$OUT_TXT"
git ls-files | while read -r f; do
  echo -e "\n\n----- FILE: $f -----\n" >> "$OUT_TXT"
  sed -n '1,400p' "$f" >> "$OUT_TXT" || true
done
if command -v pandoc >/dev/null 2>&1; then
  pandoc "$OUT_TXT" -o FULL_CODE_DUMP.docx --standalone || true
fi
zip -r FULL_CODE_DUMP.zip "$OUT_TXT" app *.kts *.gradle **/*.kt **/*.xml || true
echo "Wrote FULL_CODE_DUMP.txt (+ .docx/.zip when possible)"
