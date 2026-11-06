package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.OAuthRepository
import net.asere.heightwander.server.account.domain.SessionRepository
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.model.canAccessWhenPublicAccessDisabled
import net.asere.heightwander.server.account.domain.model.mappable.Session
import net.asere.heightwander.server.shared.core.domain.environment.Environment
import net.asere.heightwander.server.shared.core.domain.tools.formatGoogleUserName
import net.asere.heightwander.server.shared.core.domain.tools.generateId
import net.asere.heightwander.server.shared.core.domain.tools.generateRandomUserName
import net.asere.heightwander.server.shared.profile.domain.ProfileService

class LoginWithGoogle(
    private val oAuthRepository: OAuthRepository,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val profileService: ProfileService,
    private val sendVerificationEmail: SendVerificationEmail,
    private val environment: Environment,
) {
    suspend operator fun invoke(
        idToken: String,
        acceptedTerms: Boolean = false,
    ): Result {
        val googleUser =
            oAuthRepository.verifyGoogleToken(idToken)
                ?: return Result.InvalidToken

        var user =
            userRepository.getByEmail(googleUser.email).let { existing ->
                if (existing == null) {
                    if (!acceptedTerms) return Result.NeedsTermsConfirmation
                    if (!environment.isPublicAccessEnabled()) return Result.AccessDisabled
                    val newUser =
                        User(
                            id = generateId(),
                            externalId = googleUser.userId,
                            name =
                            googleUser.name?.formatGoogleUserName()
                                ?: googleUser.givenName?.formatGoogleUserName()
                                ?: generateRandomUserName(),
                            email = googleUser.email,
                            password = null,
                            type = User.Type.Customer,
                            source = User.Source.Google,
                            emailVerified = googleUser.emailVerified,
                        )
                    userRepository.save(newUser).also { storedUser ->
                        profileService.createProfile(
                            userId = storedUser.id,
                            username = storedUser.name,
                            pictureUrl = googleUser.pictureUrl,
                        )
                    }
                } else {
                    existing
                }
            }

        if (user.source != User.Source.Google) {
            return Result.WrongSource
        }

        if (googleUser.emailVerified && !user.emailVerified) {
            user = user.copy(emailVerified = true)
            userRepository.updateUser(user)
        }

        if (!user.emailVerified) {
            sendVerificationEmail(user)
            return Result.EmailNotVerified
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

        object InvalidToken : Result()

        object WrongSource : Result()

        object EmailNotVerified : Result()

        object AccessDisabled : Result()

        object NeedsTermsConfirmation : Result()
    }
}
