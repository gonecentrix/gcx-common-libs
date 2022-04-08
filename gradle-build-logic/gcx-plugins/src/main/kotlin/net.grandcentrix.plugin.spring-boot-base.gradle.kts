import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("org.springframework.boot")
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

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
}
