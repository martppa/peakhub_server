package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.extension.isValidEmail
import net.asere.heightwander.server.account.domain.extension.isValidPassword
import net.asere.heightwander.server.account.domain.extension.isValidUsername
import net.asere.heightwander.server.account.domain.model.CypherInterface
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.shared.core.domain.environment.Environment
import net.asere.heightwander.server.shared.core.domain.tools.generateId
import net.asere.heightwander.server.shared.profile.domain.ProfileService

class RegisterUser(
    private val cypherInterface: CypherInterface,
    private val userRepository: UserRepository,
    private val profileService: ProfileService,
    private val sendVerificationEmail: SendVerificationEmail,
    private val environment: Environment,
) {
    sealed interface Result {
        data object PendingVerification : Result

        data object InvalidUsername : Result

        data object InvalidPassword : Result

        data object InvalidEmail : Result

        data object UserAlreadyExist : Result

        data object AccessDisabled : Result
    }

    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        isCoach: Boolean,
    ): Result {
        if (!environment.isPublicAccessEnabled()) return Result.AccessDisabled

        if (!username.isValidUsername()) return Result.InvalidUsername
        if (!email.isValidEmail()) return Result.InvalidEmail
        if (!password.isValidPassword()) return Result.InvalidPassword
        val userWithEmail = userRepository.getByEmail(email)
        val userWithName = userRepository.getByName(username)
        if (userWithEmail != null || userWithName != null) {
            // Same response as a new signup to avoid account enumeration.
            return Result.PendingVerification
        }
        val hashedPassword = cypherInterface.hash(password)
        val user =
            User(
                id = generateId(),
                name = username,
                email = email,
                password = hashedPassword,
                type = User.Type.Customer,
                source = User.Source.Email,
                emailVerified = false,
            )
        val storedUser = userRepository.save(user)
        profileService.createProfile(
            userId = storedUser.id,
            username = storedUser.name,
        )
        sendVerificationEmail(storedUser)
        return Result.PendingVerification
    }
}
