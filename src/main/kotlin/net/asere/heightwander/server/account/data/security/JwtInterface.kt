package net.asere.heightwander.server.account.data.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*
import net.asere.heightwander.server.account.domain.TokenInterface
import net.asere.heightwander.server.account.domain.model.Token
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.model.mappable.InvalidTokenError
import net.asere.heightwander.server.shared.core.domain.environment.Environment

class JwtInterface(
    environment: Environment,
) : TokenInterface {
    companion object {
        private const val ACCESS_EXP = 60 * 60 * 1000
        private const val REFRESH_EXP = 7 * 24 * 60 * 60 * 1000
        private const val ISSUER = "heightwander"
        private const val AUDIENCE = "heightwander-api"
        private const val MIN_JWT_KEY_BYTES = 32
    }

    private val algorithm =
        Algorithm.HMAC512(
            environment.getJwtTokenKey().also { key ->
                require(key.toByteArray(Charsets.UTF_8).size >= MIN_JWT_KEY_BYTES) {
                    "JWT_TOKEN_KEY must be at least $MIN_JWT_KEY_BYTES bytes"
                }
            },
        )

    override fun createAccess(user: User): String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withJWTId(UUID.randomUUID().toString())
        .withClaim("userId", user.id)
        .withClaim("userType", user.type.toString())
        .withClaim("tokenType", Token.Type.Access.toString())
        .withClaim("tokenVersion", user.tokenVersion)
        .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_EXP))
        .sign(algorithm)

    override fun createRefresh(user: User): String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withJWTId(UUID.randomUUID().toString())
        .withClaim("userId", user.id)
        .withClaim("tokenType", Token.Type.Refresh.toString())
        .withClaim("userType", user.type.toString())
        .withClaim("tokenVersion", user.tokenVersion)
        .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_EXP))
        .sign(algorithm)

    override fun verify(token: String): Token {
        val verifier =
            JWT
                .require(algorithm)
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .build()
        val decoded =
            try {
                verifier.verify(token)
            } catch (unused: JWTVerificationException) {
                throw InvalidTokenError
            }
        return Token(
            userId = decoded.claims["userId"]!!.asString(),
            userType = User.Type.valueOf(decoded.claims["userType"]!!.asString()),
            tokenType = Token.Type.valueOf(decoded.claims["tokenType"]!!.asString()),
            tokenVersion = decoded.claims["tokenVersion"]?.asInt() ?: 0,
            expires = decoded.expiresAt.time,
            value = token,
        )
    }
}
