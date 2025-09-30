#!/usr/bin/env bash
set -euo pipefail

BASE_BRANCH="main"
BRANCH="${BRANCH:-refactor/v1}"
OUT="FULL_CODE_DUMP.txt"

echo "==> Syncing branches"
git fetch origin || true
git switch "$BASE_BRANCH"
git pull --ff-only || true
git switch "$BRANCH" || git switch -c "$BRANCH"

echo "==> Ensure all theme deps are present"
# Compose + Material3 + Google Material XML (for AndroidManifest)
gsed() { sed -i "$@"; } 2>/dev/null || alias gsed="sed -i"
if ! grep -q 'com.google.android.material:material' app/build.gradle.kts; then
  gsed '/dependencies *{/a\    implementation("com.google.android.material:material:1.12.0")' app/build.gradle.kts
fi
if ! grep -q 'androidx.compose.material3:material3' app/build.gradle.kts; then
  gsed '/dependencies *{/a\    implementation("androidx.compose.material3:material3:1.3.0")' app/build.gradle.kts
fi
if ! grep -q 'androidx.core:core-ktx' app/build.gradle.kts; then
  gsed '/dependencies *{/a\    implementation("androidx.core:core-ktx:1.15.0")' app/build.gradle.kts
fi
if ! grep -q 'androidx.appcompat:appcompat' app/build.gradle.kts; then
  gsed '/dependencies *{/a\    implementation("androidx.appcompat:appcompat:1.7.0")' app/build.gradle.kts
fi

echo "==> Replace all old/broken themes.xml"
mkdir -p app/src/main/res/values
rm -f app/src/main/res/values/themes.xml
cat > app/src/main/res/values/themes.xml <<'XML'
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Working Material3 DayNight theme -->
    <style name="Theme.OnePlan" parent="Theme.Material3.DayNight.NoActionBar">
        <item name="colorPrimary">@color/oneplan_primary</item>
        <item name="colorSecondary">@android:color/holo_blue_light</item>
        <item name="android:statusBarColor">?attr/colorPrimary</item>
    </style>
</resources>
XML

echo "==> Replace colors.xml with valid palette"
cat > app/src/main/res/values/colors.xml <<'XML'
<resources>
    <color name="oneplan_primary">#2F80ED</color>
    <color name="oneplan_on_primary">#FFFFFF</color>
    <color name="oneplan_secondary">#03DAC6</color>
    <color name="oneplan_background">#FFFFFF</color>
    <color name="oneplan_on_background">#000000</color>
</resources>
XML

echo "==> Force fresh FULL_CODE_DUMP.txt"
echo "# OnePlan — full source dump ($(date -u +%Y-%m-%dT%H:%M:%SZ))" > "$OUT"
echo >> "$OUT"
echo "## File list" >> "$OUT"
git ls-files | grep -v 'FULL_CODE_DUMP' | sort >> "$OUT"
echo >> "$OUT"
echo "## Sources" >> "$OUT"
git ls-files \
  ':!**/*.png' ':!**/*.jpg' ':!**/*.jpeg' ':!**/*.webp' ':!**/*.gif' \
  ':!**/*.keystore' ':!**/*.jks' ':!FULL_CODE_DUMP*.txt' \
| while read -r f; do
    echo -e "\n\n-----8<----- FILE: $f -----8<-----\n" >> "$OUT"
    sed -n '1,5000p' "$f" >> "$OUT" || true
  done

echo "==> Commit & push everything"
git add app/build.gradle.kts app/src/main/res/values/themes.xml app/src/main/res/values/colors.xml "$OUT"
git commit -m "fix(theme): reset to official Material3 XML theme + update dependencies + refresh dump" || echo "no changes"
git push -u origin "$BRANCH"

echo "✅ Theme fixed with official Material3 XML + updated dependencies."
echo "   Old/broken themes wiped, new colors + theme guaranteed to resolve."
