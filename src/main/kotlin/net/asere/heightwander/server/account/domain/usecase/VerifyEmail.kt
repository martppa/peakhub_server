package net.asere.heightwander.server.account.domain.usecase

import java.util.*
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.VerificationRepository
import net.asere.heightwander.server.shared.account.domain.model.error.UserDoesNotExistError

private const val CODE_EXPIRACY_TIME = 86_400_000 // 24h

class VerifyEmail(
    private val userRepository: UserRepository,
    private val verificationRepository: VerificationRepository,
) {
    suspend operator fun invoke(code: String): Result {
        if (code.isBlank()) return Result.InvalidVerificationCode
        val verificationCode = verificationRepository.getCode(code) ?: return Result.InvalidVerificationCode
        val user = userRepository.getById(verificationCode.userId) ?: throw UserDoesNotExistError
        if (Date().time - verificationCode.createdAt.time > CODE_EXPIRACY_TIME) {
            return Result.InvalidVerificationCode
        }
        userRepository.updateUser(user.copy(emailVerified = true))
        verificationRepository.removeCode(verificationCode.userId)
        return Result.Success
    }

    sealed interface Result {
        data object Success : Result

        data object InvalidVerificationCode : Result
    }
}
