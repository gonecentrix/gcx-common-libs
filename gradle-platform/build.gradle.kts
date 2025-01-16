group = "net.grandcentrix.component"

val kotlinVersion = "2.0.20"
val ktLint = "12.1.2"
val springDependencyManagement = "1.1.7"
val owaspDependencyCheck = "12.0.0"
val gradleVersions = "0.51.0"
val springBoot = "3.4.1"
val detekt = "1.23.7"
val logbackJson = "0.1.5"
val logbackEncoder = "8.0"
val kotlinLogging = "7.0.3"
val sentry = "7.20.0"
val assertK = "0.28.1"
val springMockK = "4.0.2"
val springDoc = "2.2.0"
val jUnitJupiter = "5.11.4"
val testContainers = "1.20.4"
val cyclonedxVersion = "2.0.0"
val bouncyCastleVersion = "1.80"

plugins {
    `java-platform`
    `maven-publish`
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
    api("org.springframework.boot:spring-boot-testcontainers:${springBoot}")
    api("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiter")
    api("org.junit.jupiter:junit-jupiter-api:$jUnitJupiter")
    api("com.willowtreeapps.assertk:assertk-jvm:$assertK")
    api("com.ninja-squad:springmockk:$springMockK")
    api("org.springdoc:springdoc-openapi-ui:$springDoc")
    api("org.springdoc:springdoc-openapi-kotlin:$springDoc")
    api("org.springdoc:springdoc-openapi-data-rest:$springDoc")
    api("org.springdoc:springdoc-openapi-webmvc-core:$springDoc")
    api("org.springdoc:springdoc-openapi-security:$springDoc")
    api("io.sentry:sentry:$sentry")
    api("io.github.oshai:kotlin-logging-jvm:$kotlinLogging")
    api("ch.qos.logback.contrib:logback-json-classic:$logbackJson")
    api("ch.qos.logback.contrib:logback-jackson:$logbackJson")
    api("net.logstash.logback:logstash-logback-encoder:$logbackEncoder")
    api("org.cyclonedx:cyclonedx-gradle-plugin:$cyclonedxVersion")
    api("org.bouncycastle:bcpkix-jdk18on:$bouncyCastleVersion")

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
