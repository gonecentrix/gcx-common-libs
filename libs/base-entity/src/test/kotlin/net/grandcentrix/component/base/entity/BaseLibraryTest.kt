package net.grandcentrix.component.base.entity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ContextConfiguration

@SpringBootApplication
open class EmptyContext

@SpringBootTest(classes = [EmptyContext::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration
@AutoConfigureDataJpa
@EntityScan
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaRepositories(considerNestedRepositories = true)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseLibraryTest
