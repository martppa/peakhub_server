package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.extension.isValidEmail
import net.asere.heightwander.server.account.domain.extension.isValidPassword
import net.asere.heightwander.server.account.domain.extension.isValidUsername
import net.asere.heightwander.server.account.domain.model.CypherInterface
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.shared.profile.domain.ProfileService

class AdminUpdateUser(
    private val cypherInterface: CypherInterface,
    private val userRepository: UserRepository,
    private val profileService: ProfileService,
) {
    suspend operator fun invoke(user: User): Result {
        if (!user.name.isValidUsername()) return Result.InvalidUsername
        if (!user.email.isValidEmail()) return Result.InvalidEmail
        if (!user.password.isNullOrBlank() && !user.password.isValidPassword()) {
            return Result.InvalidPassword
        }
        val storedUser = userRepository.getById(user.id) ?: return Result.NotFound
        val userWithEmail = userRepository.getByEmail(user.email)
        val userWithName = userRepository.getByName(user.name)
        if ((userWithEmail?.email != storedUser.email && userWithEmail?.email != null) ||
            (userWithName?.name != storedUser.name && userWithName?.name != null)
        ) {
            return Result.AlreadyExists
        }
        val hashedPassword =
            if (!user.password.isNullOrBlank()) {
                cypherInterface.hash(user.password)
            } else {
                storedUser.password
            }
        val passwordChanged = !user.password.isNullOrBlank()
        val updatedUser =
            storedUser.copy(
                name = user.name,
                email = user.email,
                type = user.type,
                password = hashedPassword,
                emailVerified = user.emailVerified,
                tokenVersion =
                if (passwordChanged) {
                    storedUser.tokenVersion + 1
                } else {
                    storedUser.tokenVersion
                },
            )
        userRepository.updateUser(updatedUser)
        if (updatedUser.name != storedUser.name) {
            profileService.updateUsername(
                userId = updatedUser.id,
                username = updatedUser.name,
            )
        }
        return Result.Success
    }

    sealed interface Result {
        data object Success : Result

        data object NotFound : Result

        data object AlreadyExists : Result

        data object InvalidUsername : Result

        data object InvalidEmail : Result

        data object InvalidPassword : Result
    }
}
