import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("org.jlleitschuh.gradle.ktlint")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))

    testImplementation(platform("net.grandcentrix.component:gradle-platform"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_21.majorVersion
    }

    // A wrongly formatted file cannot be build
    // This is harsh, but it fails quickly and
    // ensures it is always used
    dependsOn(tasks.ktlintFormat)
}
