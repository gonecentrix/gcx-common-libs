group = "net.grandcentrix.plugin"

plugins {
    `kotlin-dsl`
    `maven-publish`
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${properties["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:${properties["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin.plugin.jpa:org.jetbrains.kotlin.plugin.jpa.gradle.plugin:${properties["kotlinVersion"]}")
    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:${properties["springBootVersion"]}")
    implementation("org.jlleitschuh.gradle.ktlint:org.jlleitschuh.gradle.ktlint.gradle.plugin:${properties["ktLintVersion"]}")
    implementation("org.owasp.dependencycheck:org.owasp.dependencycheck.gradle.plugin:${properties["owaspDependencyCheckVersion"]}")
    implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:${properties["springDependencyManagementVersion"]}")
    implementation("com.github.ben-manes.versions:com.github.ben-manes.versions.gradle.plugin:${properties["gradleVersionsVersion"]}")
    implementation("io.gitlab.arturbosch.detekt:io.gitlab.arturbosch.detekt.gradle.plugin:${properties["detektVersion"]}")
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
