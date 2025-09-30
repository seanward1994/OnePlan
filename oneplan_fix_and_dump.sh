#!/usr/bin/env bash
set -euo pipefail

# 0) Always operate from repo ROOT
ROOT="$(git rev-parse --show-toplevel 2>/dev/null || pwd)"
cd "$ROOT"

echo "📍 Repo root: $ROOT"

# 1) Guard rails
test -d app || { echo "❌ No app/ module found"; exit 1; }

BRANCH="hotfix/material3-theme-$(date -u +%Y%m%d-%H%M%S)"
git switch -c "$BRANCH" || git checkout -b "$BRANCH" || true

# 2) Make sure Gradle wrapper exists and is executable
chmod +x gradlew || true

# 3) Patch app/build.gradle.kts (idempotent)
APP_GR="$ROOT/app/build.gradle.kts"
if ! grep -q "com.google.android.material:material" "$APP_GR" 2>/dev/null; then
  echo "🔧 Patching dependencies in app/build.gradle.kts"
  # Ensure common blocks exist
  grep -q "android \{" "$APP_GR" || sed -i '1i android {\n}\n' "$APP_GR"
  grep -q "dependencies \{" "$APP_GR" || printf "\n%s\n" "dependencies { }" >> "$APP_GR"

  # compileSdk / defaultConfig
  sed -i -E '
    /android \{/,/^\}/ {
      s/compileSdk\s*=\s*[0-9]+/compileSdk = 35/;
      /compileSdk/!b
    }
  ' "$APP_GR"

  # If defaultConfig block missing, create it
  if ! awk '/android \{/{f=1} f && /defaultConfig \{/ {print; exit}' "$APP_GR" >/dev/null; then
    sed -i '/android \{/a\    defaultConfig {\n        minSdk = 24\n        targetSdk = 35\n    }\n' "$APP_GR"
  else
    sed -i -E '
      /android \{/,/^\}/ {
        /defaultConfig \{/,/^\s*\}/ {
          s/minSdk\s*=\s*[0-9]+/minSdk = 24/;
          s/targetSdk\s*=\s*[0-9]+/targetSdk = 35/;
        }
      }' "$APP_GR"
  fi

  # Enable Compose
  if ! grep -q "buildFeatures" "$APP_GR"; then
    sed -i '/android \{/a\    buildFeatures {\n        compose = true\n    }\n' "$APP_GR"
  else
    sed -i '/buildFeatures \{/,/}/ s/compose\s*=\s*(false|0)/compose = true/' "$APP_GR"
  fi
  if ! grep -q "composeOptions" "$APP_GR"; then
    sed -i '/android \{/a\    composeOptions {\n        kotlinCompilerExtensionVersion = "1.6.11"\n    }\n' "$APP_GR"
  fi

  # Kotlin JVM toolchain (required by newer AGP)
  if ! grep -q "kotlinOptions" "$APP_GR"; then
    sed -i '/android \{/a\    kotlinOptions {\n        jvmTarget = "17"\n    }\n' "$APP_GR"
  fi

  # Add dependencies
  awk '
    BEGIN {printDep=1}
    {print}
    /dependencies \{/ && printDep {
      print "    implementation(\"com.google.android.material:material:1.12.0\")"
      print "    implementation(platform(\"androidx.compose:compose-bom:2024.09.02\"))"
      print "    implementation(\"androidx.activity:activity-compose:1.9.2\")"
      print "    implementation(\"androidx.compose.ui:ui\")"
      print "    implementation(\"androidx.compose.material3:material3\")"
      print "    implementation(\"androidx.compose.ui:ui-tooling-preview\")"
      print "    debugImplementation(\"androidx.compose.ui:ui-tooling\")"
      print "    implementation(\"androidx.lifecycle:lifecycle-runtime-ktx:2.8.6\")"
      printDep=0
    }
  ' "$APP_GR" > "$APP_GR.tmp" && mv "$APP_GR.tmp" "$APP_GR"
fi

# 4) Create Material3 XML theme + colors if missing
VAL_DIR="$ROOT/app/src/main/res/values"
mkdir -p "$VAL_DIR"

THEME_XML="$VAL_DIR/themes.xml"
if ! test -f "$THEME_XML"; then
cat > "$THEME_XML" <<XML
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.OnePlan" parent="Theme.Material3.DayNight.NoActionBar">
        <item name="android:statusBarColor">?attr/colorSurface</item>
        <item name="android:windowLightStatusBar">false</item>
        <item name="colorPrimary">@color/m3_ref_palette_primary40</item>
        <item name="colorSecondary">@color/m3_ref_palette_secondary40</item>
    </style>
</resources>
XML
  echo "🆕 Wrote values/themes.xml"
fi

COLORS_XML="$VAL_DIR/colors.xml"
if ! test -f "$COLORS_XML"; then
cat > "$COLORS_XML" <<XML
<resources>
    <color name="m3_ref_palette_primary40">#5E8AFF</color>
    <color name="m3_ref_palette_secondary40">#5BC0BE</color>
</resources>
XML
  echo "🆕 Wrote values/colors.xml"
fi

# 5) Point the manifest at Theme.OnePlan
MANIFEST="$ROOT/app/src/main/AndroidManifest.xml"
if test -f "$MANIFEST"; then
  if grep -q 'android:theme=' "$MANIFEST"; then
    sed -i -E 's/android:theme="[^"]+"/android:theme="@style\/Theme.OnePlan"/' "$MANIFEST"
  else
    # insert theme attribute on <application ...>
    sed -i -E 's/<application /<application android:theme="@style\/Theme.OnePlan" /' "$MANIFEST"
  fi
  echo "✅ Manifest now uses @style/Theme.OnePlan"
fi

# 6) Try to build
echo "🏗️  Building debug APK..."
if ./gradlew --no-daemon --stacktrace :app:assembleDebug; then
  echo "✅ Build succeeded."
else
  echo "⚠️ Build failed — continuing to produce FULL_CODE_DUMP.txt"
fi

# 7) Always produce FULL_CODE_DUMP.txt locally
OUT="$ROOT/FULL_CODE_DUMP.txt"
{
  echo "OnePlan full source dump — $(date -u +%Y-%m-%dT%H:%M:%SZ)"
  echo
  echo "### File list"
  git ls-files | sort
  echo
  echo "### Sources"
  git ls-files \
    ':!**/*.png' ':!**/*.jpg' ':!**/*.jpeg' ':!**/*.webp' ':!**/*.gif' \
    ':!**/*.apk' ':!**/*.aab' ':!**/*.keystore' ':!**/*.jks' | \
  while read -r f; do
    echo -e "\n-----8<----- FILE: $f -----8<-----\n"
    sed -n '1,500p' "$f" || true
  done
} > "$OUT"
echo "📝 Wrote $OUT ($(wc -c < "$OUT") bytes)"

# 8) Commit & push
git add -A
git commit -m "fix(material3): add Material lib + XML theme; point manifest; ensure Compose + SDK; dump"
git push -u origin "$BRANCH" || true

echo "✅ Done. Branch: $BRANCH"
echo "   Open GitHub → Actions → latest run. APK (if built) + use FULL_CODE_DUMP.txt locally."
