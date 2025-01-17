group = "net.grandcentrix.component"

plugins {
    id("net.grandcentrix.plugin.spring-boot-lib")
    id("net.grandcentrix.plugin.github-publish")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation("org.bouncycastle:bcpkix-jdk18on")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

cyclonedx {
    projectType = "library"
}
