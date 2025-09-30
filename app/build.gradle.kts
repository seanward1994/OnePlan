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
        val vc = (System.getenv("VC") ?: runCatching {
            file("${rootDir}/.oneplan_versioncode").takeIf { it.exists() }?.readText()?.trim()
        }.getOrNull() ?: "1").toInt()
        versionCode = vc
        versionName = "0.4.$vc"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug { isDebuggable = true }
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.12" }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.08.00")
    implementation(composeBom); androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Compose UI + M3
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Required for XML theme parent Theme.Material3.DayNight.NoActionBar
    implementation("com.google.android.material:material:1.12.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Room + KSP
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Debug/Test
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
