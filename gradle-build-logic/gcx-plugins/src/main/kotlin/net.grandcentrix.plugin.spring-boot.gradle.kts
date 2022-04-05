plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("org.springframework.boot")
    id("com.github.ben-manes.versions")
    id("org.owasp.dependencycheck")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
}

allOpen {
    /*
     We need to make sure that classes annotated with these JPA annotations are open. Otherwise, JPA will fetch the
     entities always eager and ignore FetchType.LAZY silently. For more information see this bugticket:
     https://youtrack.jetbrains.com/issue/KT-28525
     */
    annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embeddable")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("noarg"))
    implementation(kotlin("reflect"))
    implementation(kotlin("allopen"))

    api("org.springframework.boot:spring-boot-starter-data-jpa")
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
