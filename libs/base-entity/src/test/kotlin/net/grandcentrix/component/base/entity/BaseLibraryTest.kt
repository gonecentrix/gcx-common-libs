package net.grandcentrix.component.base.entity

import net.grandcentrix.component.base.config.DataSourceProperties
import net.grandcentrix.component.base.entity.example.ComplexEntityRepository
import net.grandcentrix.component.base.repository.RepositoryWithExclusiveLock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ConfigurationPropertiesScan("net.grandcentrix.component.base.config")
open class EmptyContext

@EntityScan
@ComponentScan(basePackageClasses = [RepositoryWithExclusiveLock::class])
@EnableJpaRepositories(
    considerNestedRepositories = true,
    basePackageClasses = [ComplexEntityRepository::class]
)
@Import(DataSourceProperties::class)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class BaseLibraryTest
