import groovy.xml.dom.DOMCategory.attributes

plugins {
    id("net.grandcentrix.plugin.spring-boot-base")
}

dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
}

fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS,
): String = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
    .directory(workingDir)
    .redirectOutput(ProcessBuilder.Redirect.PIPE)
    .redirectError(ProcessBuilder.Redirect.PIPE)
    .start()
    .apply { waitFor(timeoutAmount, timeoutUnit) }
    .run {
        val error = errorStream.bufferedReader().readText().trim()
        if (error.isNotEmpty()) {
            throw Exception(error)
        }
        inputStream.bufferedReader().readText().trim()
    }

val gitHash = "git rev-parse HEAD".runCommand(rootDir)
val gitBranch = "git rev-parse --abbrev-ref HEAD".runCommand(rootDir)

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Git-Hash" to gitHash,
                "git-Branch" to gitBranch,
            )
        )
    }
    enabled = true
}

tasks.bootJar {
    enabled = false
}
