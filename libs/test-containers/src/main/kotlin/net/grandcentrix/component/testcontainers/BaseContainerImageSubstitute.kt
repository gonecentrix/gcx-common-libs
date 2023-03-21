package net.grandcentrix.component.testcontainers

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.ImageNameSubstitutor

/**
 * Base implementation of a org.testcontainers.utility.ImageNameSubstitutor
 * Replaces the static PostgreSQL image version by a custom one for the base entity tests
 *
 * To enable this, just extend this class with the desired image and version and include it into
 * the properties file `testcontainers.properties` in your test resources' folder
 *
 * See more in: https://www.testcontainers.org/features/image_name_substitution/
 */
abstract class BaseContainerImageSubstitute(
    private val imageName: String = PostgreSQLContainer.IMAGE,
    private val imageVersion: String
) : ImageNameSubstitutor() {

    override fun apply(original: DockerImageName): DockerImageName =
        if (original.repository.equals(PostgreSQLContainer.IMAGE)) {
            DockerImageName.parse("$imageName:$imageVersion")
        } else {
            original
        }

    override fun getDescription() = "Replaces PostgreSQL image with $imageName and $imageVersion"
}
