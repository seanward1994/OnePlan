plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}
android {
  namespace = "com.oneplan.app"
  compileSdk = 34
  defaultConfig {
    applicationId = "com.oneplan.app"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "0.1.0"
  }
  buildTypes {
    release { isMinifyEnabled = false }
  }
  compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
  kotlinOptions { jvmTarget = "17" }
}
dependencies {
  implementation(platform("androidx.compose:compose-bom:2024.09.03"))
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.activity:activity-compose:1.9.2")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.material3:material3")
}
