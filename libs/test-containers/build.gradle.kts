group = "net.grandcentrix.component"

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("net.grandcentrix.plugin.spring-boot-lib")
    id("net.grandcentrix.plugin.github-publish")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    implementation(platform("org.testcontainers:testcontainers-bom"))
    implementation("org.testcontainers:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-testcontainers")
    api("org.testcontainers:postgresql")
    api("org.testcontainers:testcontainers")

    implementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
}
