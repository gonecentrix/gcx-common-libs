group = "net.grandcentrix.component"

plugins {
    id("net.grandcentrix.plugin.spring-boot-lib")
    id("net.grandcentrix.plugin.github-publish")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
}

cyclonedx {
    projectType = "library"
}
