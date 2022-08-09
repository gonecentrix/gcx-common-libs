# GCX Testcontainers

This library provides abstract classes that can be extended and used in your tests:

```mermaid
classDiagram
BaseIntegrationTest <|-- BaseDatabaseIntegrationTest
BaseDatabaseIntegrationTest <|-- BaseDataJpaIntegrationTest
BaseDatabaseIntegrationTest <|-- BaseSpringBootIntegrationTest
BaseContainerImageSubstitute <|-- ContainerImageSubstituteExample
BaseContainerImageSubstitute <|-- [YourProjectContainerImageSubstitute]
```
- `BaseContainerImageSubstitute`
  - Can be used to replace the image name and version of the PostgreSQL container used in all integration tests
- `BaseIntegrationTest`
- `BaseDatabaseIntegrationTest`
  - Includes a singleton postgres testcontainer version *12-alpine*
- `BaseDataJpaIntegrationTest`
  - Extends `BaseDatabaseIntegrationTest`
  - Adds auto-configuration to use test database instead of the application database
  - Adds `@DataJpaTest` annotation which replaces full autoconfiguration and adds only the auto-configurations related to JPA tests
  - This class can be used to test the persistence layer of your application
- `BaseSpringBootIntegrationTest`
  - Extends `BaseDatabaseIntegrationTest`
  - Adds `@SpringBootTest` annotation for full Spring Boot integration tests
  - This class can be used for full integration tests as it contains an application context that simulates one of an application running in a production environment

## How to use it

- Add the dependencies:
```
dependencies {
    implementation(platform("net.grandcentrix.component:gradle-platform"))
    testImplementation("net.grandcentrix.component:test-containers")
    runtimeOnly("org.postgresql:postgresql")
}
```
- Extend one of the classes above in your tests. A simple example of Spring Boot integration tests is shown below:
```kotlin
class ExampleOfIntegrationTest(
    @Autowired private val userRepository: UserRepository
): BaseSpringBootIntegrationTest() {
    
    @Test
    fun `saves user in repository successfully`() {
        assertThat {
            userRepository.save(User())
        }.isSuccess()
    }
}
```
> Other examples can be found in tests of the `base-entity` library.

## How to change the postgres image version

In order to change the default image version from `postgres:12-alpine`, you can just extend the [`BaseContainerImageSubstitute`](src/main/kotlin/net/grandcentrix/component/testcontainers/BaseContainerImageSubstitute.kt)
This abstract class provides a basic image name and version replacement function for your convenience. 

An example can be found in [`ContainerImageSubstituteExample`](../base-entity/src/test/kotlin/net/grandcentrix/component/base/entity/ContainerImageSubstituteExample.kt).
> Make sure to include a `testcontainers.properties` file with the appropriate configuration. For this example, the content of that file  would be:

```
image.substitutor=net.grandcentrix.component.base.entity.ContainerImageSubstituteExample
```
