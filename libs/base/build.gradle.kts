group = "net.grandcentrix.component"

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("net.grandcentrix.plugin.spring-boot-lib")
    id("net.grandcentrix.plugin.github-publish")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    testImplementation("net.grandcentrix.component:test-containers")
    testRuntimeOnly("org.postgresql:postgresql")
}
