package net.asere.heightwander.server.account.data.security

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import java.util.regex.Pattern
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Hash passwords for storage, and test passwords against password tokens.
 *
 * New hashes use PBKDF2-HMAC-SHA256 (`$32$`). Legacy `$31$` (SHA1) hashes are
 * still verified so existing accounts keep working.
 */
class PasswordAuthentication
@JvmOverloads
constructor(
    cost: Int = DEFAULT_COST,
) {
    private val random: SecureRandom
    private val cost: Int

    init {
        iterations(cost)
        this.cost = cost
        random = SecureRandom()
    }

    fun hash(password: CharArray): String {
        val salt = ByteArray(SIZE / 8)
        random.nextBytes(salt)
        val dk = pbkdf2(password, salt, 1 shl cost, ALGORITHM)
        val hash = ByteArray(salt.size + dk.size)
        System.arraycopy(salt, 0, hash, 0, salt.size)
        System.arraycopy(dk, 0, hash, salt.size, dk.size)
        val enc = Base64.getUrlEncoder().withoutPadding()
        return ID + cost + '$' + enc.encodeToString(hash)
    }

    fun authenticate(
        password: CharArray,
        token: String,
    ): Boolean {
        val algorithm =
            when {
                token.startsWith(ID) -> ALGORITHM
                token.startsWith(ID_LEGACY) -> ALGORITHM_LEGACY
                else -> return false
            }
        val m = layout.matcher(token)
        if (!m.matches()) return false
        val iterations = iterations(m.group(1).toInt())
        val hash = Base64.getUrlDecoder().decode(m.group(2))
        val salt = Arrays.copyOfRange(hash, 0, SIZE / 8)
        val check = pbkdf2(password, salt, iterations, algorithm)
        var zero = 0
        for (idx in check.indices) zero = zero or (hash[salt.size + idx].toInt() xor check[idx].toInt())
        return zero == 0
    }

    fun needsRehash(token: String): Boolean = !token.startsWith(ID)

    @Deprecated("Use {@link #hash(char[])} instead", ReplaceWith("hash(password.toCharArray())"))
    fun hash(password: String): String = hash(password.toCharArray())

    @Deprecated(
        """Use {@link #authenticate(char[],String)} instead.
      """,
        ReplaceWith("authenticate(password.toCharArray(), token)"),
    )
    fun authenticate(
        password: String,
        token: String,
    ): Boolean = authenticate(password.toCharArray(), token)

    companion object {
        /** Current token prefix (PBKDF2-HMAC-SHA256). */
        const val ID = "$32$"

        /** Legacy token prefix (PBKDF2-HMAC-SHA1). */
        const val ID_LEGACY = "$31$"

        const val DEFAULT_COST = 16
        private const val ALGORITHM = "PBKDF2WithHmacSHA256"
        private const val ALGORITHM_LEGACY = "PBKDF2WithHmacSHA1"
        private const val SIZE = 128
        private val layout = Pattern.compile("\\$(?:31|32)\\$(\\d\\d?)\\$(.{43})")

        private fun iterations(cost: Int): Int {
            require(!(cost < 0 || cost > 30)) { "cost: $cost" }
            return 1 shl cost
        }

        private fun pbkdf2(
            password: CharArray,
            salt: ByteArray,
            iterations: Int,
            algorithm: String,
        ): ByteArray {
            val spec: KeySpec = PBEKeySpec(password, salt, iterations, SIZE)
            return try {
                val f = SecretKeyFactory.getInstance(algorithm)
                f.generateSecret(spec).encoded
            } catch (ex: NoSuchAlgorithmException) {
                throw IllegalStateException("Missing algorithm: $algorithm", ex)
            } catch (ex: InvalidKeySpecException) {
                throw IllegalStateException("Invalid SecretKeyFactory", ex)
            }
        }
    }
}
