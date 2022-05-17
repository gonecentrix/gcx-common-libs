plugins {
    id("net.grandcentrix.plugin.spring-boot-lib")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
}
