object Project {
    const val Platform = "gradle-platform"
    const val Plugins = "gradle-build-logic"
    const val Base = "base"
}

val allLibProjects = listOf(Project.Base, Project.Plugins)

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
