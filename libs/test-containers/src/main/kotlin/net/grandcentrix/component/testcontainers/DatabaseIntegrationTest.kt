package net.grandcentrix.component.testcontainers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.PropertySource
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import java.util.function.Supplier

@ActiveProfiles("test")
@ContextConfiguration(initializers = [DatabaseIntegrationTest.DatabasePropertyInitializer::class])
annotation class DatabaseIntegrationTest {
    companion object {
        private const val postgresVersion = "14-apline"

        @JvmStatic
        val postgreSqlContainer =
            PostgreSQLContainer<Nothing>(
                "${PostgreSQLContainer.IMAGE}:$postgresVersion"
            ).apply {
                start()
            }
    }

    class DatabasePropertySource : PropertySource<MutableMap<String, Supplier<Any>>>(
        "testContainerDatabaseProperties",
        HashMap()
    ) {
        override fun getProperty(name: String): Any? {
            return source[name]?.get()
        }

        fun add(name: String, value: Supplier<Any>) {
            source[name] = value
        }
    }

    class DatabasePropertyInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        private val springDataSourceProperty = "spring.datasource"

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            applicationContext.environment.propertySources.addFirst(
                DatabasePropertySource().apply {
                    add("$springDataSourceProperty.url", postgreSqlContainer::getJdbcUrl)
                    add("$springDataSourceProperty.username", postgreSqlContainer::getUsername)
                    add("$springDataSourceProperty.password", postgreSqlContainer::getPassword)
                }
            )
        }
    }
}
