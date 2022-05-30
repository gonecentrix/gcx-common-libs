package net.grandcentrix.component.base.entity.autoconfigure

import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration(proxyBeanMethods = false)
@Import(JpaAuditingConfiguration::class)
@AutoConfigureAfter(WebMvcAutoConfiguration::class)
class BaseAutoConfiguration
