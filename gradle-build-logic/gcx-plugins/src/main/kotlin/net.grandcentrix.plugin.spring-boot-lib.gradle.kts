plugins {
    id("net.grandcentrix.plugin.spring-boot-base")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
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
