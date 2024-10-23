package net.grandcentrix.component.tls

import jakarta.annotation.PostConstruct
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.Security
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

/**
 * This component configures BouncyCastle as security provider and allows creating SSLContext to be used for
 * TLS secured connections.
 */
@Component
class SSLContextProvider {
    @PostConstruct
    fun setup() {
        Security.addProvider(BouncyCastleProvider())
    }

    /**
     * This creates a java.net.ssl.SSLContext used for verification of the server certificate. No client
     * verification can be achieved with this.
     * @param caPath The path to the PEM encoded file containing the trusted CA certificates to verify server
     *               certificates against
     */
    fun create(caPath: String) = createContext(caPath = caPath, crtPath = null, keyPath = null, password = null)

    /**
     * This creates a java.net.ssl.SSLContext used for mTLS. Both the server and the client will be able to validate
     * the identity of each other.
     *
     * @param caPath The path to the PEM encoded file containing the trusted CA certificates to verify s
     *               erver certificates against
     * @param crtPath The path to the PEM encoded file containing the client certificate chain presented from the
     *                client to the server during handshake
     * @param keyPath The path to the PEM encoded file containing the private key belonging to the client leaf
     *                certificate identifying this client
     * @param password Optional password for the KeyStore, usually not set
     */
    fun create(
        caPath: String,
        crtPath: String,
        keyPath: String,
        password: String? = null,
    ) = createContext(caPath, crtPath, keyPath, password)

    /**
     * This method creates a java.net.ssl.SSLContext based on the given certificates and keys. This can be used
     * to authenticate either as a server or a client, depending on the provided certificates. Currently only one
     * certificate chain is supported and the provided are not reloaded during runtime when changed.
     *
     * If only the caPath is provided this context can only be used to verify the identity of remote parties.
     *
     * If caPath, crtPath and keyPath are provided this context can be used for full mTLS.
     *
     * @param caPath The path to the PEM encoded CA certificate to trust
     * @param crtPath The path to the file containing one or multiple PEM encoded certificates to identify as
     * @param keyPath The path to the PEM encoded private key matching the public key in the leaf certificate of the previous certificate chain
     * @param password Optional password for the underlying KeyStore
     */
    private fun createContext(
        caPath: String,
        crtPath: String?,
        keyPath: String?,
        password: String?,
    ): SSLContext {
        require(caPath.isNotEmpty()) { "No CAs to trust are specified, but TLS usage is enabled" }
        val trustManagerFactory =
            FileInputStream(caPath).use {
                CertificateFactory.getInstance("X.509").generateCertificates(it).toList()
            }.let {
                getTrustManagerFactory(it)
            }

        val crtCerts =
            if (!crtPath.isNullOrEmpty()) {
                require(!keyPath.isNullOrEmpty()) {
                    "Specifying certificates to identify this peer without a private is very likely an invalid configuration"
                }
                FileInputStream(crtPath).use {
                    CertificateFactory.getInstance("X.509").generateCertificates(it).toList()
                }
            } else {
                listOf()
            }

        val keyManagerFactory =
            if (!keyPath.isNullOrEmpty()) {
                require(
                    crtCerts.isNotEmpty(),
                ) { "A private key is specified, but no certificates. This is very likely an invalid configuration" }
                FileInputStream(keyPath).use {
                    when (val keyObject = PEMParser(it.reader()).readObject()) {
                        is PEMKeyPair -> JcaPEMKeyConverter().getKeyPair(keyObject).private
                        is PrivateKeyInfo -> JcaPEMKeyConverter().getPrivateKey(keyObject)
                        else -> error("Unknown result of PEMParser.readObject: ${it.javaClass}")
                    }
                }.let {
                    getKeyManagerFactory(crtCerts, it, password ?: "")
                }
            } else {
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                    init(
                        KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                            load(null, null)
                        },
                        "".toCharArray(),
                    )
                }
            }

        return SSLContext.getInstance("TLSv1.3").apply {
            init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, SecureRandom.getInstanceStrong())
        }
    }

    private fun getKeyManagerFactory(
        crts: List<Certificate>,
        key: PrivateKey?,
        password: String,
    ): KeyManagerFactory =
        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
            init(
                KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                    load(null, null)
                    crts.forEachIndexed { index, cert -> setCertificateEntry("cert-$index", cert) }
                    setKeyEntry("key", key, password.toCharArray(), crts.toTypedArray())
                },
                password.toCharArray(),
            )
        }

    private fun getTrustManagerFactory(caList: List<Certificate>): TrustManagerFactory =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(
                KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                    load(null, null)
                    caList.forEachIndexed { index, cert -> setCertificateEntry("ca-$index", cert) }
                },
            )
        }
}
