import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    id("io.gitlab.arturbosch.detekt")
}

apply(plugin = "io.gitlab.arturbosch.detekt")

val detektDependency: Configuration by configurations.creating {
    isTransitive = false
}
dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    detektDependency("net.grandcentrix.component:artur-bosch-detekt:+")
}

tasks.register<Copy>("copyDetektConfigFiles") {
    group = "build"
    description = "Copies baseline.xml and detekt.xml logging files into the target resource folder"
    val destination = layout.projectDirectory.dir("config/detekt")
    from(zipTree(detektDependency.singleFile)) {
        exclude("META-INF/**")
    }
    into(destination)
    eachFile {
        if (destination.file(name).asFile.exists()) {
            exclude()
        }
    }
}

detekt {
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    baseline = file("$rootDir/config/detekt/baseline.xml")
}

tasks.getByName("sourcesJar") {
    dependsOn("copyDetektConfigFiles")
}

tasks.getByName("detekt") {
    dependsOn("copyDetektConfigFiles")
}

tasks.build {
    dependsOn("copyDetektConfigFiles")
}

tasks.processResources {
    dependsOn("copyDetektConfigFiles")
}

val analysisDir = file(projectDir)
val baselineFile = file("$rootDir/config/detekt/baseline.xml")
val configFile = file("$rootDir/config/detekt/detekt.yml")

val kotlinFiles = "**/*.kt"
val kotlinScriptFiles = "**/*.kts"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"
val testFiles = "**/*Test.kt"

val detektAll by tasks.registering(Detekt::class) {
    description = "Runs the whole project at once."
    parallel = true
    buildUponDefaultConfig = true
    setSource(analysisDir)
    config.setFrom(listOf(configFile))
    include(kotlinFiles)
    include(kotlinScriptFiles)
    exclude(resourceFiles)
    exclude(buildFiles)
    exclude(testFiles)
    baseline.set(baselineFile)
}

val detektProjectBaseline by tasks.registering(DetektCreateBaselineTask::class) {
    description = "Overrides current baseline."
    buildUponDefaultConfig.set(true)
    ignoreFailures.set(true)
    parallel.set(true)
    setSource(analysisDir)
    config.setFrom(listOf(configFile))
    include(kotlinFiles)
    include(kotlinScriptFiles)
    exclude(resourceFiles)
    exclude(buildFiles)
    exclude(testFiles)
    baseline.set(baselineFile)
}
