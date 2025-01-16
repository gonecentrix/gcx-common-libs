group = "net.grandcentrix.component"

plugins {
    id("net.grandcentrix.plugin.spring-boot-lib")
    id("net.grandcentrix.plugin.github-publish")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    testImplementation("net.grandcentrix.component:test-containers")
    testRuntimeOnly("org.postgresql:postgresql")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

cyclonedx {
    projectType = "library"
}
