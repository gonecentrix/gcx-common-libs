group = "net.grandcentrix.component"

val kotlinVersion = "1.8.10"
val ktLint = "10.2.1"
val springDependencyManagement = "1.0.11.RELEASE"
val gradleVersions = "0.42.0"
val owaspDependencyCheck = "7.0.0"
val springBoot = "3.0.2"
val logbackJson = "0.1.5"
val kotlinLogging = "2.1.21"
val sentry = "6.13.1"
val assertK = "0.25"
val springMockK = "3.1.1"
val springDoc = "1.6.9"
val detekt = "1.19.0"
val spring = "6.0.5"
val jUnit = "5.9.0"
val testContainers = "1.17.3"

plugins {
    `java-platform`
    `maven-publish`
}

javaPlatform {
    allowDependencies()
}

dependencies.constraints {
    api("$group:base-entity")
    api("$group:test-containers")
    api("$group:kotlin-logger")
    api("$group:artur-bosch-detekt")

    api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:$kotlinVersion")
    api("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:$kotlinVersion")
    api("org.jetbrains.kotlin.plugin.jpa:org.jetbrains.kotlin.plugin.jpa.gradle.plugin:$kotlinVersion")
    api("org.jetbrains.kotlin.plugin.kotlin-reflect:org.jetbrains.kotlin.plugin.kotlin-reflect.gradle.plugin:$kotlinVersion")
    api("org.springframework.boot:org.springframework.boot.gradle.plugin:$springBoot")
    api("io.gitlab.arturbosch.detekt:io.gitlab.arturbosch.detekt.gradle.plugin:$detekt")
    api("org.jlleitschuh.gradle.ktlint:org.jlleitschuh.gradle.ktlint.gradle.plugin:$ktLint")
    api("com.github.ben-manes.versions:com.github.ben-manes.versions.gradle.plugin:$gradleVersions")
    api("org.owasp.dependencycheck:org.owasp.dependencycheck.gradle.plugin:$owaspDependencyCheck")
    api("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:$springDependencyManagement")
    api("org.springframework.boot:spring-boot-starter-data-jpa:$springBoot")
    api("org.springframework.boot:spring-boot-starter-test:$springBoot")
    api("org.springframework.boot:spring-boot-test:$springBoot")
    api("org.springframework:spring-test:$spring")
    api("org.junit.jupiter:junit-jupiter-engine:$jUnit")
    api("org.junit.jupiter:junit-jupiter-api:$jUnit")
    api("com.willowtreeapps.assertk:assertk-jvm:$assertK")
    api("com.ninja-squad:springmockk:$springMockK")
    api("org.springdoc:springdoc-openapi-ui:$springDoc")
    api("org.springdoc:springdoc-openapi-kotlin:$springDoc")
    api("org.springdoc:springdoc-openapi-data-rest:$springDoc")
    api("org.springdoc:springdoc-openapi-webmvc-core:$springDoc")
    api("org.springdoc:springdoc-openapi-security:$springDoc")
    api("io.sentry:sentry:$sentry")
    api("io.github.microutils:kotlin-logging-jvm:$kotlinLogging")
    api("ch.qos.logback.contrib:logback-json-classic:$logbackJson")
    api("ch.qos.logback.contrib:logback-jackson:$logbackJson")

    api("org.testcontainers:testcontainers-bom:$testContainers")
    api("org.testcontainers:junit-jupiter:$testContainers")
    api("org.testcontainers:postgresql:$testContainers")
    api("org.testcontainers:testcontainers:$testContainers")
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
    publications {
        create<MavenPublication>(project.name) {

            // Skips loading java components from gradle-platform, as it does not contain any
            if (!project.name.contains("gradle-platform"))
                from(components["java"])

            groupId = project.group.toString()
            artifactId = project.name

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}
