package net.grandcentrix.component.base.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import net.grandcentrix.component.testcontainers.DataJpaIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.testcontainers.containers.PostgreSQLContainer

@DataJpaIntegrationTest
class ContainerImageSubstituteExampleTest(
    @Autowired private val postgreSqlContainer: PostgreSQLContainer<Nothing>
) {

    @Test
    fun `when image name substitute is configured, the container is created using that image`() {
        val expectedImage = "${PostgreSQLContainer.IMAGE}:${ContainerImageSubstituteExample.IMAGE_VERSION}"
        assertThat(postgreSqlContainer.image.get().toString()).isEqualTo(expectedImage)
    }
}
