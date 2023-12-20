package net.grandcentrix.component.testcontainers

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Target(AnnotationTarget.CLASS)
annotation class IntegrationTest

/**
 * Annotate test classes with this annotation to create JPA slice tests with a reusable test
 * container setup. Please note that this annotation only provides the test container and JPA
 * slice test setup, any project specific configuration like which JPA repositories needs to be
 * with additional annotations/configuration classes.
 */
@Target(AnnotationTarget.CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseIntegrationTest
annotation class DataJpaIntegrationTest

/**
 * Annotate test classes with this annotation to create full spring boot tests with test
 * container support. Please note that this annotation only provides the test container and JPA
 * slice test setup, any project specific configuration like which JPA repositories needs to be
 * with additional annotations/configuration classes.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseIntegrationTest
@Target(AnnotationTarget.CLASS)
annotation class SpringBootIntegrationTest
