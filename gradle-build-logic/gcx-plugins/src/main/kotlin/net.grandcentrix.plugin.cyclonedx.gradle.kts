import org.gradle.kotlin.dsl.invoke

plugins {
    id("org.cyclonedx.bom")
}

apply(plugin = "org.cyclonedx.bom")

interface CyclonedxConfig {
    val projectType: Property<String>
    val componentName: Property<String>
    val componentVersion: Property<String>
}

val extension = project.extensions.create<CyclonedxConfig>("cyclonedx")

extension.projectType.convention("application")
extension.componentName.convention(project.name)
extension.componentVersion.convention(project.version.toString())

tasks.cyclonedxBom {
    setIncludeConfigs(setOf("runtimeClasspath"))
    setProjectType(extension.projectType.get())
    setComponentName(extension.componentName.get())
    setComponentVersion(extension.componentVersion.get())
    setOutputFormat("json")
    setOutputName("bom")
}
