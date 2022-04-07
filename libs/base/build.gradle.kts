group = "net.grandcentrix.component"

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("net.grandcentrix.plugin.spring-boot-lib")
    id("net.grandcentrix.plugin.github-publish")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    testImplementation(platform("org.testcontainers:testcontainers-bom"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers")

    testRuntimeOnly("org.postgresql:postgresql")
}
