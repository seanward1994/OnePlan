// Root build.gradle.kts — keep it minimal to avoid duplicate `plugins {}` issues.
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Optional conveniences
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
