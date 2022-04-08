plugins {
    id("net.grandcentrix.plugin.spring-boot-base")
    id("com.github.ben-manes.versions")
    id("org.owasp.dependencycheck")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }

    annotationProcessor(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
