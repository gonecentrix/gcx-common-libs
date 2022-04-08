import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("net.grandcentrix.plugin.spring-boot-base")
    kotlin("plugin.jpa")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    api(kotlin("stdlib-jdk8"))
    api(kotlin("noarg"))
    api(kotlin("reflect"))

    api("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
    enabled = true
}

tasks.bootJar {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = sourceCompatibility
    }
}
