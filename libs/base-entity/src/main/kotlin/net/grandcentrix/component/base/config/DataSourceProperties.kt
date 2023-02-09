package net.grandcentrix.component.base.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("spring.datasource")
@ConstructorBinding
data class DataSourceProperties(
    val lockTimeoutMs: Long = 3_000,
)
