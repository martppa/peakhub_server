package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.OAuthRepository
import net.asere.heightwander.server.account.domain.SessionRepository
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.model.canAccessWhenPublicAccessDisabled
import net.asere.heightwander.server.account.domain.model.mappable.Session
import net.asere.heightwander.server.shared.core.domain.environment.Environment
import net.asere.heightwander.server.shared.core.domain.tools.generateId
import net.asere.heightwander.server.shared.core.domain.tools.generateRandomUserName
import net.asere.heightwander.server.shared.profile.domain.ProfileService

class LoginWithApple(
    private val oAuthRepository: OAuthRepository,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val profileService: ProfileService,
    private val environment: Environment,
) {
    suspend operator fun invoke(
        idToken: String,
        acceptedTerms: Boolean = false,
    ): Result {
        val appleUser =
            oAuthRepository.verifyAppleToken(idToken)
                ?: return Result.InvalidToken

        var user =
            userRepository.getByExternalId(appleUser.userId)
                ?: appleUser.email?.let { userRepository.getByEmail(it) }

        if (user == null) {
            val email = appleUser.email ?: return Result.InvalidToken
            if (!acceptedTerms) return Result.NeedsTermsConfirmation
            if (!environment.isPublicAccessEnabled()) return Result.AccessDisabled
            val newUser =
                User(
                    id = generateId(),
                    externalId = appleUser.userId,
                    name = appleUser.name?.takeIf { it.isNotBlank() } ?: generateRandomUserName(),
                    email = email,
                    password = null,
                    type = User.Type.Customer,
                    source = User.Source.Apple,
                    emailVerified = true,
                )
            user =
                userRepository.save(newUser).also { storedUser ->
                    profileService.createProfile(
                        userId = storedUser.id,
                        username = storedUser.name,
                        pictureUrl = null,
                    )
                }
        } else {
            if (user.source != User.Source.Apple) {
                return Result.WrongSource
            }
            // Backfill externalId / email verification for older rows
            var updated = user
            if (updated.externalId.isNullOrBlank()) {
                updated = updated.copy(externalId = appleUser.userId)
            }
            if (!updated.emailVerified) {
                updated = updated.copy(emailVerified = true)
            }
            if (appleUser.email != null && updated.email != appleUser.email) {
                // Keep existing email; Apple may omit or change relay presentation
            }
            if (updated != user) {
                userRepository.updateUser(updated)
                user = updated
            }
        }

        if (!environment.isPublicAccessEnabled() && !user.type.canAccessWhenPublicAccessDisabled) {
            return Result.AccessDisabled
        }

        val session = sessionRepository.createSession(user)
        return Result.Success(session)
    }

    sealed class Result {
        data class Success(
            val session: Session,
        ) : Result()

        data object InvalidToken : Result()

        data object WrongSource : Result()

        data object AccessDisabled : Result()

        data object NeedsTermsConfirmation : Result()
    }
}
