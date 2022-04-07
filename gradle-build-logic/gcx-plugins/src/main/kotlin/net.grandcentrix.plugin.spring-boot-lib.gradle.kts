import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("org.springframework.boot")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
}

allOpen {
    /*
     We need to make sure that classes annotated with these JPA annotations are open. Otherwise, JPA will fetch the
     entities always eager and ignore FetchType.LAZY silently. For more information see this bugticket:
     https://youtrack.jetbrains.com/issue/KT-28525
     */
    annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embeddable")
}


dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    api(kotlin("stdlib-jdk8"))
    api(kotlin("noarg"))
    api(kotlin("reflect"))
    api(kotlin("allopen"))

    api("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
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
