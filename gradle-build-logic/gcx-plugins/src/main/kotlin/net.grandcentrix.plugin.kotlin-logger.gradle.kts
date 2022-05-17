plugins {
    id("net.grandcentrix.plugin.kotlin-base")
}

val kotlinLogger: Configuration by configurations.creating {
    isTransitive = false
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    api("io.github.microutils:kotlin-logging-jvm")
    runtimeOnly("ch.qos.logback.contrib:logback-json-classic")
    runtimeOnly("ch.qos.logback.contrib:logback-jackson")
    kotlinLogger("net.grandcentrix.component:kotlin-logger:+")
}

tasks.register<Copy>("copyLoggingConfigFiles") {
    group = "build"
    description = "Copies log-json.xml and logback-spring.xml logging files into the target resource folder"
    val destination = layout.projectDirectory.dir("src/main/resources")
    from(zipTree(kotlinLogger.singleFile)) {
        exclude("META-INF/**")
    }
    into(destination)
    eachFile {
        if (destination.file(name).asFile.exists()) {
            exclude()
        }
    }
}

tasks.getByName("sourcesJar") {
    dependsOn("copyLoggingConfigFiles")
}

tasks.build {
    dependsOn("copyLoggingConfigFiles")
}

tasks.processResources {
    dependsOn("copyLoggingConfigFiles")
}
