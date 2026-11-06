package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.extension.isValidEmail

class ResendVerificationEmail(
    private val userRepository: UserRepository,
    private val sendVerificationEmail: SendVerificationEmail,
) {
    suspend operator fun invoke(email: String): Result {
        if (!email.isValidEmail()) return Result.InvalidEmail
        val user = userRepository.getByEmail(email) ?: return Result.Success
        if (!user.emailVerified) {
            sendVerificationEmail(user)
        }
        return Result.Success
    }

    sealed interface Result {
        data object Success : Result

        data object InvalidEmail : Result
    }
}
