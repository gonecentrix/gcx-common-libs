group = "net.grandcentrix.component"

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("net.grandcentrix.plugin.github-publish")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

cyclonedx {
    projectType = "library"
}
