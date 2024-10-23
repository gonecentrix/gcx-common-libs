object Project {
    const val Platform = "gradle-platform"
    const val Plugins = "gradle-build-logic"
    const val BaseEntity = "base-entity"
    const val TestContainers = "test-containers"
    const val GcxLogging = "kotlin-logger"
    const val Detekt = "artur-bosch-detekt"
    const val TlsHelper = "tls-helper"
}

val allLibProjects = listOf(
    Project.BaseEntity, Project.TestContainers, Project.GcxLogging, Project.Detekt,
    Project.TlsHelper, Project.Plugins
)

tasks.register("build") {
    group = "build"
    description = "Build all projects"
    allLibProjects.forEach { project ->
        dependsOn(gradle.includedBuild(project).task(":build"))
    }
}

tasks.register("clean") {
    group = "build"
    description = "Clean all projects"
    allLibProjects.forEach { project ->
        dependsOn(gradle.includedBuild(project).task(":clean"))
    }
}

tasks.register("check") {
    group = "verification"
    description = "Run Kotlin Linter"
    allLibProjects.forEach { project ->
        dependsOn(gradle.includedBuild(project).task(":check"))
    }
}

tasks.register("ktlintCheck") {
    group = "verification"
    description = "Run Kotlin Linter"
    allLibProjects.forEach { project ->
        dependsOn(gradle.includedBuild(project).task(":ktlintCheck"))
    }
}

tasks.register("test") {
    group = "verification"
    description = "Run all tests"
    allLibProjects.forEach { project ->
        dependsOn(gradle.includedBuild(project).task(":test"))
    }
}

tasks.register("publish") {
    group = "publishing"
    description = "Publishes all components"
    dependsOn(gradle.includedBuild(Project.Platform).task(":publish"))
    allLibProjects.forEach { project ->
        dependsOn(gradle.includedBuild(project).task(":publish"))
    }
}

tasks.register("cyclonedxBom") {
    group = "reporting"
    description = "Assembles a CycloneDX SBOM for component"
    allLibProjects.forEach { project ->
        dependsOn(gradle.includedBuild(project).task(":cyclonedxBom"))
    }
}

tasks.register("ktlintFormat") {
    group = "formatting"
    description = "Run Kotlin Linter"
    allLibProjects.filter { projectName -> projectName != "gradle-build-logic" }.forEach { project ->
        dependsOn(gradle.includedBuild(project).task(":ktlintFormat"))
    }
}
