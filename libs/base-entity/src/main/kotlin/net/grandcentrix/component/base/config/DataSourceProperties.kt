package net.grandcentrix.component.base.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.convert.DurationUnit
import java.time.Duration
import java.time.temporal.ChronoUnit

@ConfigurationProperties("grandcentrix.datasource")
data class DataSourceProperties(

    @DurationUnit(ChronoUnit.MILLIS)
    val lockTimeout: Duration = Duration.ofMillis(3_000)
)
