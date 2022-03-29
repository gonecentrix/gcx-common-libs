dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

pluginManagement {
    includeBuild("../../gradle-plugins")
}

includeBuild("../../gradle-platform")
