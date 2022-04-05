import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("org.jlleitschuh.gradle.ktlint")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm")
    testImplementation("com.ninja-squad:springmockk")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = sourceCompatibility
    }

    // A wrongly formatted file cannot be build
    // This is harsh, but it fails quickly and
    // ensures it is always used
    dependsOn(tasks.ktlintFormat)
}
