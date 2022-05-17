dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

pluginManagement {
    includeBuild("../../gradle-build-logic")
}

includeBuild("../../gradle-platform")
