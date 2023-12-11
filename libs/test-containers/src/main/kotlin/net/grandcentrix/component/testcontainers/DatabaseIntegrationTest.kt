package net.grandcentrix.component.testcontainers

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.PostgreSQLContainer

/**
 * Annotate test classes use a single test container postgres database for your integration tests
 * The PostgreSQL version used here is `14-alpine`
 * If you would like to change this version you need to extend a BaseContainerImageSubstitute
 * and add a `testcontainers.properties` file referencing your image name substitute:
 *
 * net.grandcentrix.component.base.entity.ContainerImageSubstituteExample
 *
 * @see net.grandcentrix.component.base.entity.ContainerImageSubstituteExample
 * @see net.grandcentrix.component.testcontainers.BaseContainerImageSubstitute
 */

@Target(AnnotationTarget.CLASS)
@IntegrationTest
@Import(DatabaseIntegrationTest.DatabaseTestConfiguration::class)
annotation class DatabaseIntegrationTest {

    @Configuration
    @Profile("test")
    class DatabaseTestConfiguration {
        private val postgresVersion = "14-alpine"

        @Bean
        @ServiceConnection
        fun postgresContainer(): PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>(
            "${PostgreSQLContainer.IMAGE}:$postgresVersion"
        ).apply {
            start()
        }
    }
}
