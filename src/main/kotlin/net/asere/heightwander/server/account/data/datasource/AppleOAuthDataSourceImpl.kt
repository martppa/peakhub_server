package net.asere.heightwander.server.account.data.datasource

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.net.URI
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.asere.heightwander.server.account.domain.model.AppleUser
import net.asere.heightwander.server.shared.core.domain.environment.Environment

class AppleOAuthDataSourceImpl(
    private val environment: Environment,
) : AppleOAuthDataSource {
    private val jwkProvider =
        JwkProviderBuilder(URI("https://appleid.apple.com/auth/keys").toURL())
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()

    override suspend fun verifyAppleToken(idToken: String): AppleUser? = withContext(Dispatchers.IO) {
        try {
            val decoded = JWT.decode(idToken)
            val jwk = jwkProvider.get(decoded.keyId)
            val publicKey = jwk.publicKey as RSAPublicKey
            val algorithm = Algorithm.RSA256(publicKey, null)
            val verified =
                JWT
                    .require(algorithm)
                    .withIssuer("https://appleid.apple.com")
                    .withAudience(environment.getAppleClientId())
                    .build()
                    .verify(idToken)
            verified.toAppleUser()
        } catch (_: Exception) {
            null
        }
    }

    private fun DecodedJWT.toAppleUser(): AppleUser {
        val email = getClaim("email").asString()
        val emailVerifiedClaim = getClaim("email_verified")
        val emailVerified =
            when {
                emailVerifiedClaim.isNull -> false
                emailVerifiedClaim.asBoolean() != null -> emailVerifiedClaim.asBoolean()
                else -> emailVerifiedClaim.asString()?.equals("true", ignoreCase = true) == true
            }
        return AppleUser(
            userId = subject,
            email = email,
            emailVerified = emailVerified,
            name = getClaim("name").asString(),
        )
    }
}
