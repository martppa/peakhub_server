package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.SessionRepository
import net.asere.heightwander.server.account.domain.model.Token
import net.asere.heightwander.server.account.domain.model.canAccessWhenPublicAccessDisabled
import net.asere.heightwander.server.account.domain.model.mappable.AccessDisabledError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidTokenError
import net.asere.heightwander.server.account.domain.model.mappable.Session
import net.asere.heightwander.server.shared.core.domain.environment.Environment
import net.asere.heightwander.server.shared.core.domain.model.error.BadRequestError

class RefreshSession(
    private val decodeToken: DecodeToken,
    private val getUserById: GetUserById,
    private val sessionRepository: SessionRepository,
    private val environment: Environment,
) {
    suspend operator fun invoke(refreshToken: String): Session {
        val token = decodeToken(refreshToken)
        if (token.tokenType != Token.Type.Refresh) {
            throw BadRequestError("Invalid token type")
        }
        val user = getUserById(token.userId)
        if (token.tokenVersion != user.tokenVersion) {
            throw InvalidTokenError
        }
        if (!environment.isPublicAccessEnabled() && !user.type.canAccessWhenPublicAccessDisabled) {
            throw AccessDisabledError
        }
        return sessionRepository.createSession(user)
    }
}
