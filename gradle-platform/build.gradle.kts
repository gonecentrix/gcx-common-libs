group = "net.grandcentrix.component"

val kotlinVersion = project.properties["kotlinVersion"]
val ktLint = project.properties["ktLintVersion"]
val springDependencyManagement = project.properties["springDependencyManagementVersion"]
val gradleVersions = project.properties["gradleVersionsVersion"]
val owaspDependencyCheck = project.properties["owaspDependencyCheckVersion"]
val springBoot = project.properties["springBootVersion"]
val logbackJson = project.properties["logbackJsonVersion"]
val kotlinLogging = project.properties["kotlinLoggingVersion"]
val sentry = project.properties["sentryVersion"]
val assertK = project.properties["assertKVersion"]
val springMockK = project.properties["springMockKVersion"]
val springDoc = project.properties["springDocVersion"]
val detekt = project.properties["detektVersion"]
val spring = project.properties["springVersion"]
val jUnit = project.properties["jUnitVersion"]
val testContainers = project.properties["testContainersVersion"]

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
