package net.asere.heightwander.server.account.domain.usecase

import java.util.*
import net.asere.heightwander.server.account.domain.RecoveryRepository
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.extension.isValidPassword
import net.asere.heightwander.server.shared.account.domain.model.error.UserDoesNotExistError

private const val CODE_EXPIRACY_TIME = 3_600_000 // 1h

class ResetPassword(
    private val userRepository: UserRepository,
    private val updateUser: AdminUpdateUser,
    private val recoveryCodeRepository: RecoveryRepository,
) {
    suspend operator fun invoke(
        code: String,
        newPassword: String,
    ): Result {
        if (!newPassword.isValidPassword()) return Result.InvalidPassword
        val recoveryCode = recoveryCodeRepository.getCode(code) ?: return Result.InvalidRecoveryCode
        val user = userRepository.getById(recoveryCode.userId) ?: throw UserDoesNotExistError
        if (Date().time - recoveryCode.createdAt.time > CODE_EXPIRACY_TIME) {
            return Result.InvalidRecoveryCode
        }
        updateUser(user.copy(password = newPassword))
        recoveryCodeRepository.removeCode(recoveryCode.userId)
        return Result.Success
    }

    sealed interface Result {
        data object Success : Result

        data object InvalidRecoveryCode : Result

        data object InvalidPassword : Result
    }
}
