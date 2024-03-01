package net.grandcentrix.component.tls.autoconfigure

import net.grandcentrix.component.tls.SSLContextProvider
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(SSLContextProvider::class)
class SSLAutoConfiguration
