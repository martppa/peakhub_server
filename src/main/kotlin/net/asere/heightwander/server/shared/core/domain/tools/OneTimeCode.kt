package net.asere.heightwander.server.shared.core.domain.tools

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * High-entropy one-time codes for email recovery / verification.
 * Persist [digest] only; email the plaintext from [generate].
 */
object OneTimeCode {
    private val secureRandom = SecureRandom()
    private val encoder = Base64.getUrlEncoder().withoutPadding()

    /** 256-bit URL-safe token suitable for query-string links. */
    fun generate(): String {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return encoder.encodeToString(bytes)
    }

    fun digest(plaintext: String): String {
        val hash =
            MessageDigest
                .getInstance("SHA-256")
                .digest(plaintext.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { byte -> "%02x".format(byte) }
    }
}
