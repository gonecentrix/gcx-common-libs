import gradle.kotlin.dsl.accessors._4fbff5071b8ed094c65db737a1ab055e.cyclonedxBom

plugins {
    id("org.cyclonedx.bom")
}

apply(plugin = "org.cyclonedx.bom")

tasks.cyclonedxBom {
    setIncludeConfigs(listOf("runtimeClasspath", "compileClasspath", "testCompileClasspath"))
    setProjectType("library")
}
