// Root build.gradle.kts — keep minimal; no plugins{} here.
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
tasks.register("clean", Delete::class) { delete(rootProject.buildDir) }
