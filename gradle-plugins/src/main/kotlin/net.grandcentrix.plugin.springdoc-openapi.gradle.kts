group = "net.grandcentrix.plugin"

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    api("org.springdoc:springdoc-openapi-ui")
    api("org.springdoc:springdoc-openapi-data-rest")
    api("org.springdoc:springdoc-openapi-security")
    api("org.springdoc:springdoc-openapi-kotlin")
}
