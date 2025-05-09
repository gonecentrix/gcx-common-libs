group = "net.grandcentrix.component"

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("net.grandcentrix.plugin.github-publish")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))

    implementation("net.logstash.logback:logstash-logback-encoder")
}

cyclonedx {
    projectType = "library"
}
