group = "net.grandcentrix.plugin"

plugins {
    id("net.grandcentrix.plugin.kotlin-base")
    `maven-publish`
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/GCX-SI/gcx-common-libs")
            credentials {
                username = project.findProperty("github.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("github.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>(project.name) {

            // Skips loading java components from gradle-platform, as it does not contain any
            if (!project.name.contains("gradle-platform"))
                from(components["java"])

            groupId = project.group.toString()
            artifactId = project.name

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}
