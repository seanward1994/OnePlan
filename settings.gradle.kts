pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // safe for simple projects
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "OnePlanAndroid"
include(":app")
