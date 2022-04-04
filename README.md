# GCX Shared Code Monorepo

## List of Libraries

1. Base (The Base for Spring Boot Apps)

## List of Plugins
1. Detekt
2. Kotlin Base
3. Spring Boot
4. Spring Boot Lib
5. SpringDoc OpenAPI

## Add Library to your Project

1. Go to https://github.com/settings/tokens/new
2. Create a Github Token with `read:packages` permission
3. Copy the token
4. Create or edit the file `~/.gradle/gradle.properties` in your machine. You need at least the following two lines:
    ```kotlin
    github.user=<your github.com username for grandcentrix>
    github.token=<the token as copied>
    ```
5. Go back to Github and enable SSO for GCX-SI (by clicking “Authorize” next to GCX-SI and follow the instructions)
6. Add the package repository:
    ```kotlin
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/GCX-SI/gcx-common-libs")
            credentials {
                username = project.findProperty("github.user") as String? ?: System.getenv("GITHUB_USER")
                password = project.findProperty("github.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    ```
7. Add the desired dependencies:
    ```kotlin
    implementation("net.grandcentrix.component:base")
    ```

## Publishing

1. gradle publish -Pversion=[version]