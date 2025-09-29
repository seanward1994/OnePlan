// Root build.gradle.kts — minimal (no plugins block here)
allprojects { repositories { google(); mavenCentral() } }
tasks.register("clean", Delete::class) { delete(rootProject.buildDir) }
