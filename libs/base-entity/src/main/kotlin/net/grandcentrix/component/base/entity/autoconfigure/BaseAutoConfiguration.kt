package net.grandcentrix.component.base.entity.autoconfigure

import net.grandcentrix.component.base.config.DataSourceProperties
import net.grandcentrix.component.base.repository.CustomRepositoryContext
import net.grandcentrix.component.base.repository.RepositoryWithExclusiveLock
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

@AutoConfiguration
@AutoConfigureAfter(WebMvcAutoConfiguration::class)
@Import(
    JpaAuditingConfiguration::class,
    RepositoryWithExclusiveLock::class,
    CustomRepositoryContext::class,
)
@EnableConfigurationProperties(DataSourceProperties::class)
class BaseAutoConfiguration
