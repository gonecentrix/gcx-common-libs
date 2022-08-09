package net.grandcentrix.component.base.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import net.grandcentrix.component.testcontainers.BaseDatabaseIntegrationTest
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer

class ContainerImageSubstituteExampleTest : BaseDatabaseIntegrationTest() {

    @Test
    fun `when image name substitute is configured, the container is created using that image`() {
        val expectedImage = "${PostgreSQLContainer.IMAGE}:${ContainerImageSubstituteExample.IMAGE_VERSION}"
        assertThat(postgreSqlContainer.image.get().toString()).isEqualTo(expectedImage)
    }
}
