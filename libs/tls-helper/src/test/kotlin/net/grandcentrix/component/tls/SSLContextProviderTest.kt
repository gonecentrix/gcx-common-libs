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

        assertThrows<IllegalArgumentException> {
            sslContextProvider.createContext(crtPath = certPath, keyPath = keyPath, caPath = "", password = null)
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

        assertThrows<IllegalArgumentException> {
            sslContextProvider.createContext(crtPath = certPath, caPath = caCertPath, keyPath = null, password = null)
        }
    }

    @Test
    fun `Creating a SSLContext for mTLS without certificate fails`() {
        val keyPath = sslContextProvider.getResourcePath("test-files/private/server.key")
        val caCertPath = sslContextProvider.getResourcePath("test-files/certs/ca.cert")

        assertThrows<IllegalArgumentException> {
            sslContextProvider.createContext(keyPath = keyPath, caPath = caCertPath, crtPath = null, password = null)
        }
    }
}

fun SSLContextProvider.getResourcePath(fileName: String): String = javaClass.classLoader.getResource(fileName).path
