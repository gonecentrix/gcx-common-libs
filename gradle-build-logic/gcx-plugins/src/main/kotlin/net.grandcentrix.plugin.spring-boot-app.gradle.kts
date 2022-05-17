plugins {
    id("net.grandcentrix.plugin.spring-boot-base")
    id("com.github.ben-manes.versions")
    id("org.owasp.dependencycheck")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))

    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-actuator")

    api("com.fasterxml.jackson.module:jackson-module-kotlin")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
