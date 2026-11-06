package net.asere.heightwander.server.shared.core.presentation.api.auth

import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import net.asere.heightwander.server.account.domain.model.Token
import net.asere.heightwander.server.account.domain.usecase.DecodeToken
import net.asere.heightwander.server.account.domain.usecase.GetUserById

class AuthTokenResolver(
    private val decodeToken: DecodeToken,
    private val getUserById: GetUserById,
) {
    suspend fun resolveAccessToken(call: ApplicationCall): Token? {
        val raw = call.request.headers[HttpHeaders.Authorization]?.takeIf { it.isNotBlank() } ?: return null
        val tokenValue = raw.removePrefix("Bearer ").trim()
        if (tokenValue.isBlank()) return null
        return runCatching { decodeToken(tokenValue) }
            .getOrNull()
            ?.takeIfValidAccess()
    }

    private suspend fun Token.takeIfValidAccess(): Token? {
        if (tokenType != Token.Type.Access) return null
        val user = runCatching { getUserById(userId) }.getOrNull() ?: return null
        if (tokenVersion != user.tokenVersion) return null
        return this
    }
}
