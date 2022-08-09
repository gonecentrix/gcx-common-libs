package net.grandcentrix.component.testcontainers

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@ActiveProfiles("test")
abstract class BaseIntegrationTest

/**
 * Extend to use a single test container postgres database for your integration tests
 * The PostgreSQL version used here is `12-alpine`
 * If you would like to change this version you need to extend a BaseContainerImageSubstitute
 * and add a `testcontainers.properties` file referencing your image name substitute:
 *
 * net.grandcentrix.component.base.entity.ContainerImageSubstituteExample
 *
 * @see net.grandcentrix.component.base.entity.ContainerImageSubstituteExample
 * @see net.grandcentrix.component.testcontainers.BaseContainerImageSubstitute
 */
abstract class BaseDatabaseIntegrationTest : BaseIntegrationTest() {

    /**
     * Start a single [PostgreSQLContainer] that will be re-used between tests.
     * For more information on singleton containers, see https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/.
     */
    companion object {

        private const val springDataSourceProperty = "spring.dataSource"

        private const val postgreVersion = "12-alpine"

        var postgreSqlContainer = PostgreSQLContainer<Nothing>("${PostgreSQLContainer.IMAGE}:$postgreVersion").apply {
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("$springDataSourceProperty.url", postgreSqlContainer::getJdbcUrl)
            registry.add("$springDataSourceProperty.username", postgreSqlContainer::getUsername)
            registry.add("$springDataSourceProperty.password", postgreSqlContainer::getPassword)
        }
    }
}

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class BaseDataJpaIntegrationTest : BaseDatabaseIntegrationTest()

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class BaseSpringBootIntegrationTest : BaseDatabaseIntegrationTest()
