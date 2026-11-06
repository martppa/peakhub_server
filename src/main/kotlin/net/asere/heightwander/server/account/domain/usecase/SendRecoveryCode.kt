package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.RecoveryRepository
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.extension.isValidEmail
import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode
import net.asere.heightwander.server.shared.core.domain.service.EmailService
import net.asere.heightwander.server.shared.core.domain.tools.OneTimeCode
import net.asere.heightwander.server.shared.core.domain.tools.generateId

class SendRecoveryCode(
    private val userRepository: UserRepository,
    private val recoveryCodeRepository: RecoveryRepository,
    private val emailService: EmailService,
) {
    suspend operator fun invoke(email: String): Result {
        if (!email.isValidEmail()) return Result.InvalidEmail
        val user = userRepository.getByEmail(email) ?: return Result.Success
        val plaintext = OneTimeCode.generate()
        val code =
            RecoveryCode(
                id = generateId(),
                userId = user.id,
                value = plaintext,
            )
        recoveryCodeRepository.saveCode(code)
        emailService.sendRecoveryCode(code = plaintext, user.email)
        return Result.Success
    }

    sealed interface Result {
        data object Success : Result

        data object InvalidEmail : Result
    }
}
