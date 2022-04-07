package net.grandcentrix.component.base

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@ActiveProfiles("test")
abstract class BaseIntegrationTest

@Testcontainers
abstract class BaseDatabaseIntegrationTest : BaseIntegrationTest() {

    /**
     * Start a single [PostgreSQLContainer] that will be re-used between tests.
     */
    companion object {

        private const val springDataSourceProperty = "spring.dataSource"

        private const val postgreVersion = "12-alpine"

        @Container
        var postgreSqlContainer: PostgreSQLContainer<*> = NonStoppingPostgresSqlContainer(postgreVersion)

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

class NonStoppingPostgresSqlContainer(dockerTag: String) :
    PostgreSQLContainer<NonStoppingPostgresSqlContainer>("$IMAGE:$dockerTag") {

    override fun stop() {
        // do nothing (i.e. do NOT stop container between tests)
    }
}
