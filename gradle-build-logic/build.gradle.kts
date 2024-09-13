plugins {
    `kotlin-dsl`
}

repositories.gradlePluginPortal()

tasks.register("publish") {
    group = "publishing"
    description = "Publishes all components"
    dependsOn(gradle.includedBuild("gcx-plugins").task(":publish"))
}

tasks.register("cyclonedxBom") {
    group = "reporting"
    description = "Generates the SBOM of all components"
    dependsOn(gradle.includedBuild("gcx-plugins").task(":cyclonedxBom"))
}
