import org.gradle.kotlin.dsl.kotlin

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("org.springframework.boot")
    kotlin("plugin.allopen")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

allOpen {
    /*
     We need to make sure that classes annotated with these JPA annotations are open. Otherwise, JPA will fetch the
     entities always eager and ignore FetchType.LAZY silently. For more information see this bug ticket:
     https://youtrack.jetbrains.com/issue/KT-28525
     */
    annotations("jakarta.persistence.Entity", "jakarta.persistence.MappedSuperclass", "jakarta.persistence.Embeddable")
}

// 2023-01-19:
// the jpa plugin doesn't work with NoArgs constructors on `jakarta.*` paths yet,
// so we have to set it manually
noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    api(kotlin("stdlib-jdk8"))
    api(kotlin("noarg"))
    api(kotlin("reflect"))

    api("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }

    annotationProcessor(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
}
