package net.grandcentrix.component.testcontainers

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer

@ActiveProfiles("test")
@ComponentScan("net.grandcentrix.component.testcontainers")
@Target(AnnotationTarget.CLASS)
annotation class DatabaseIntegrationTest {

    @Configuration
    @Profile("test")
    class DatabaseTestConfiguration {
        private val postgresVersion = "14-alpine"

        @Bean
        @ServiceConnection
        fun postgresContainer() = PostgreSQLContainer<Nothing>(
            "${PostgreSQLContainer.IMAGE}:$postgresVersion"
        ).apply {
            start()
        }
    }
}
