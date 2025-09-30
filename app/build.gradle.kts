plugins {
    id("com.android.application") version "8.5.2"
    kotlin("android") version "1.9.24"
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
}

android {
    namespace = "com.oneplan.megaalpha"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oneplan.megaalpha"
        minSdk = 24
        targetSdk = 34
        val vcFile = file("${rootDir}/.oneplan_versioncode")
        val vcStr = if (vcFile.exists()) vcFile.readText().trim() else "1"
        versionCode = vcStr.toInt()
        versionName = "0.4.$versionCode"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
        }
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
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")

    // Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Room (KSP)
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Tests
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
