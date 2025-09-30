#!/usr/bin/env bash
set -euo pipefail

# ---------------- CONFIG ----------------
BASE_BRANCH="main"
SAFE_BRANCH="refactor/v1"
VC_FILE=".oneplan_versioncode"
OUT_DUMP="FULL_CODE_DUMP.txt"

# Ensure we are running in bash (not sh)
if [ -z "${BASH_VERSION:-}" ]; then
  echo "❌ This script must run in bash, not sh"
  exit 1
fi

# ---------------- GIT SETUP ----------------
git fetch origin || true
git switch "$BASE_BRANCH"
git pull --ff-only
git switch "$SAFE_BRANCH" || git switch -c "$SAFE_BRANCH"

# ---------------- GRADLE WRAPPER ----------------
mkdir -p gradle/wrapper
cat > gradle/wrapper/gradle-wrapper.properties <<'EOF'
distributionUrl=https\://services.gradle.org/distributions/gradle-8.7-bin.zip
EOF

# Minimal gradlew for Codespaces local run
cat > gradlew <<'EOF'
#!/usr/bin/env bash
exec ./gradle -q "$@"
EOF
chmod +x gradlew

# ---------------- ROOT GRADLE ----------------
cat > build.gradle.kts <<'KTS'
// Root kept minimal
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
KTS

# ---------------- SETTINGS ----------------
cat > settings.gradle.kts <<'KTS'
rootProject.name = "OnePlanAndroid"
include(":app")
KTS

# ---------------- APP BUILD ----------------
mkdir -p app
cat > app/build.gradle.kts <<'KTS'
plugins {
    id("com.android.application") version "8.5.2"
    kotlin("android") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
}

android {
    namespace = "com.oneplan.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oneplan.app"
        minSdk = 24
        targetSdk = 34
        val vc = (System.getenv("VC") ?: "1").toInt()
        versionCode = vc
        versionName = "0.3.$vc"
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.12" }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.08.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
KTS

# ---------------- VERSION BUMP ----------------
VC=$(( $(cat "$VC_FILE" 2>/dev/null || echo 0) + 1 ))
echo "$VC" > "$VC_FILE"
git add "$VC_FILE"

# ---------------- FULL CODE DUMP ----------------
echo "# OnePlan — full source dump ($(date -u +%Y-%m-%dT%H:%M:%SZ))" > "$OUT_DUMP"
echo >> "$OUT_DUMP"
git ls-files | sort >> "$OUT_DUMP"
echo >> "$OUT_DUMP"
git ls-files | while read -r f; do
  echo -e "\n----- FILE: $f -----\n" >> "$OUT_DUMP"
  sed -n '1,400p' "$f" >> "$OUT_DUMP" || true
done
git add "$OUT_DUMP"

# ---------------- COMMIT + PUSH ----------------
git commit -am "build: auto-bootstrap (vc=$VC)" || echo "No changes to commit"
git push origin "$SAFE_BRANCH"

echo "✅ OnePlan bootstrapped! Check Actions → CI run for APK + FULL_CODE_DUMP."