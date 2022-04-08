import java.io.File
import java.io.FileInputStream
import java.util.Properties

group = "net.grandcentrix.plugin"

plugins {
    `kotlin-dsl`
    `maven-publish`
}

val platformProperties = Properties().apply {
    load(FileInputStream(File("gradle-platform", "gradle.properties")))
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${platformProperties["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:${platformProperties["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin.plugin.jpa:org.jetbrains.kotlin.plugin.jpa.gradle.plugin:${platformProperties["kotlinVersion"]}")
    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:${platformProperties["springBootVersion"]}")
    implementation("org.jlleitschuh.gradle.ktlint:org.jlleitschuh.gradle.ktlint.gradle.plugin:${platformProperties["ktLintVersion"]}")
    implementation("org.owasp.dependencycheck:org.owasp.dependencycheck.gradle.plugin:${platformProperties["owaspDependencyCheckVersion"]}")
    implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:${platformProperties["springDependencyManagementVersion"]}")
    implementation("com.github.ben-manes.versions:com.github.ben-manes.versions.gradle.plugin:${platformProperties["gradleVersionsVersion"]}")
    implementation("io.gitlab.arturbosch.detekt:io.gitlab.arturbosch.detekt.gradle.plugin:${platformProperties["detektVersion"]}")
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
