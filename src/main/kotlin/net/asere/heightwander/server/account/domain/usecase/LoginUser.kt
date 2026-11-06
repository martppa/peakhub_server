package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.SessionRepository
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.CypherInterface
import net.asere.heightwander.server.account.domain.model.canAccessWhenPublicAccessDisabled
import net.asere.heightwander.server.account.domain.model.mappable.Session
import net.asere.heightwander.server.shared.core.domain.environment.Environment

class LoginUser(
    private val sessionRepository: SessionRepository,
    private val cypherInterface: CypherInterface,
    private val userRepository: UserRepository,
    private val environment: Environment,
) {
    suspend operator fun invoke(
        reference: String,
        password: String,
    ): Result {
        val user =
            userRepository.getByName(reference) ?: userRepository.getByEmail(reference)
                ?: return Result.InvalidCredentials
        val storedHash = user.password ?: return Result.InvalidCredentials
        val authenticated =
            cypherInterface.authenticate(
                password = password,
                hash = storedHash,
            )
        if (!authenticated) return Result.InvalidCredentials
        if (!user.emailVerified) return Result.EmailNotVerified
        if (!environment.isPublicAccessEnabled() && !user.type.canAccessWhenPublicAccessDisabled) {
            return Result.AccessDisabled
        }
        if (cypherInterface.needsRehash(storedHash)) {
            userRepository.updateUser(
                user.copy(password = cypherInterface.hash(password)),
            )
        }
        val session = sessionRepository.createSession(user)
        return Result.Success(session = session)
    }

    sealed interface Result {
        data class Success(
            val session: Session,
        ) : Result

        data object InvalidCredentials : Result

        data object SocialLoginError : Result

        data object EmailNotVerified : Result

        data object AccessDisabled : Result
    }
}
