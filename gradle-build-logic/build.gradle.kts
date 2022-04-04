plugins {
    `kotlin-dsl`
}

repositories.gradlePluginPortal()

tasks.register("publish") {
    group = "publishing"
    description = "Publishes all components"
    dependsOn(gradle.includedBuild("gcx-plugins").task(":publish"))
}
