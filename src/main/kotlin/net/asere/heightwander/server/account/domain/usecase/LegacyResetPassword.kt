package net.asere.heightwander.server.account.domain.usecase

import java.util.*
import net.asere.heightwander.server.account.domain.RecoveryRepository
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.mappable.InvalidRecoveryCodeError
import net.asere.heightwander.server.shared.account.domain.model.error.UserDoesNotExistError

private const val CODE_EXPIRACY_TIME = 3_600_000 // 1h

@Deprecated("Use ResetPassword instead")
class LegacyResetPassword(
    private val userRepository: UserRepository,
    private val updateUser: AdminUpdateUser,
    private val recoveryCodeRepository: RecoveryRepository,
) {
    suspend operator fun invoke(
        code: String,
        newPassword: String,
    ) {
        val recoveryCode = recoveryCodeRepository.getCode(code) ?: throw InvalidRecoveryCodeError
        val user = userRepository.getById(recoveryCode.userId) ?: throw UserDoesNotExistError
        if (Date().time - recoveryCode.createdAt.time > CODE_EXPIRACY_TIME) {
            throw InvalidRecoveryCodeError
        }
        updateUser(user.copy(password = newPassword))
        recoveryCodeRepository.removeCode(recoveryCode.userId)
    }
}
