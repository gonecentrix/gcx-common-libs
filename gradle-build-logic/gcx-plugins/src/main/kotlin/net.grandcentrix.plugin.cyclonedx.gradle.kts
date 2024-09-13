import org.gradle.kotlin.dsl.invoke

plugins {
    id("org.cyclonedx.bom")
}

apply(plugin = "org.cyclonedx.bom")

tasks.cyclonedxBom {
    setIncludeConfigs(listOf("runtimeClasspath", "compileClasspath", "testCompileClasspath"))
    setProjectType("library")
}
