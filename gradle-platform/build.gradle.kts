group = "net.grandcentrix.component"

val kotlinVersion = project.properties["kotlinVersion"]
val logbackJson = "0.1.5"
val kotlinLogging = "2.1.21"
val sentry = "5.6.3"
val assertK = "0.25"
val springMockK = "3.1.1"
val springDoc = "1.6.6"
val detekt = "1.19.0"
val springBoot = "2.6.6"
val spring = "5.3.17"
val jUnit = "5.8.2"
val ktLint = "10.2.1"
val postgresSql = "1.15.3"
val springDependencyManagement = "1.0.11.RELEASE"
val gradleVersions = "0.42.0"
val owaspDependencyCheck = "7.0.0"

plugins {
    `java-platform`
    `maven-publish`
}

buildscript {
    extra.set("kotlin.version", project.properties["kotlinVersion"])
}

javaPlatform {
    allowDependencies()
}

dependencies.constraints {
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

    runtime("org.testcontainers:postgresql:$postgresSql")
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
