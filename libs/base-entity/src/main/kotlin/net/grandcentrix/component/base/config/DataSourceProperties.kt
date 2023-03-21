package net.grandcentrix.component.base.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.datasource")
data class DataSourceProperties(
    val lockTimeoutMs: Long = 3_000
)
