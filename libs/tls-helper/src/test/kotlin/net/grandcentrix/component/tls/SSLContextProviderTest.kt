package net.grandcentrix.component.tls

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [SSLContextProvider::class])
class SSLContextProviderTest(
    @Autowired private val sslContextProvider: SSLContextProvider
) {

    @Test
    fun `Creating a SSLContext with only a CA certificate succeeds`() {
        val caCertPath = sslContextProvider.getResourcePath("test-files/certs/ca.cert")
        assertDoesNotThrow {
            sslContextProvider.create(caCertPath)
        }
    }

    @Test
    fun `Creating a SSLContext for mTLS without CA cert fails`() {
        val certPath = sslContextProvider.getResourcePath("test-files/certs/server.crt")
        val keyPath = sslContextProvider.getResourcePath("test-files/private/server.key")

        assertThrows<IllegalStateException> {
            sslContextProvider.create(crtPath = certPath, keyPath = keyPath, caPath = "")
        }
    }

    @Test
    fun `Creating a SSLContext for mTLS succeeds`() {
        val certPath = sslContextProvider.getResourcePath("test-files/certs/server.crt")
        val keyPath = sslContextProvider.getResourcePath("test-files/private/server.key")
        val caCertPath = sslContextProvider.getResourcePath("test-files/certs/ca.cert")

        assertDoesNotThrow {
            sslContextProvider.create(caCertPath, certPath, keyPath)
        }
    }

    @Test
    fun `Creating a SSLContext for mTLS without private key fails`() {
        val certPath = sslContextProvider.getResourcePath("test-files/certs/server.crt")
        val caCertPath = sslContextProvider.getResourcePath("test-files/certs/ca.cert")

        assertThrows<IllegalStateException> {
            sslContextProvider.create(crtPath = certPath, caPath = caCertPath)
        }
    }

    @Test
    fun `Creating a SSLContext for mTLS without certificate fails`() {
        val keyPath = sslContextProvider.getResourcePath("test-files/private/server.key")
        val caCertPath = sslContextProvider.getResourcePath("test-files/certs/ca.cert")

        assertThrows<IllegalStateException> {
            sslContextProvider.create(keyPath = keyPath, caPath = caCertPath)
        }
    }
}

fun SSLContextProvider.getResourcePath(fileName: String): String = javaClass.classLoader.getResource(fileName).path
