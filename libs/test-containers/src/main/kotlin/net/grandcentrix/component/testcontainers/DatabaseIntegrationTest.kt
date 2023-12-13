package net.grandcentrix.component.testcontainers

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
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
    @EnableJpaAuditing
    @Profile("test")
    class DatabaseTestConfiguration {

        @Bean
        @ServiceConnection
        fun postgresContainer(postgresVersion: String): PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>(
            "${PostgreSQLContainer.IMAGE}:$postgresVersion"
        ).apply {
            start()
        }

        @Bean(name = ["postgresVersion"])
        @ConditionalOnMissingBean(name = ["postgresVersion"])
        fun postgresVersion(): String = "12-alpine"
    }
}
