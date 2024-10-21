group = "net.grandcentrix.plugin"

plugins {
    `kotlin-dsl`
    `maven-publish`

    id("org.cyclonedx.bom") version "1.10.0"
}

repositories.gradlePluginPortal()

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin")
    implementation("org.jetbrains.kotlin.plugin.jpa:org.jetbrains.kotlin.plugin.jpa.gradle.plugin")
    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin")
    implementation("org.jlleitschuh.gradle.ktlint:org.jlleitschuh.gradle.ktlint.gradle.plugin")
    implementation("org.owasp.dependencycheck:org.owasp.dependencycheck.gradle.plugin")
    implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin")
    implementation("com.github.ben-manes.versions:com.github.ben-manes.versions.gradle.plugin")
    implementation("io.gitlab.arturbosch.detekt:io.gitlab.arturbosch.detekt.gradle.plugin")
    implementation("org.cyclonedx:cyclonedx-gradle-plugin")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/GCX-SI/gcx-common-libs")
            credentials {
                username = project.findProperty("github.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("github.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.cyclonedxBom {
    setIncludeConfigs(listOf("runtimeClasspath", "compileClasspath", "testCompileClasspath"))
    setProjectType("library")
}
