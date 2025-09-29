plugins {
  id("com.android.application") version "8.5.2" apply false
  id("org.jetbrains.kotlin.android") version "2.0.20" apply false
}
plugins {
    kotlin("jvm") version "1.9.24" apply false
    id("org.jetbrains.compose") version "1.7.0" apply false
}
plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "com.oneplan.desktop.MainKt"
        nativeDistributions {
            packageName = "OnePlan"
            packageVersion = "0.1.0"
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
            )
            description = "OnePlan for Windows"
            copyright = "© 2025 OnePlan"
        }
    }
}

kotlin {
    jvmToolchain(17)
}
