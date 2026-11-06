package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.RecoveryRepository
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode
import net.asere.heightwander.server.shared.core.domain.service.EmailService
import net.asere.heightwander.server.shared.core.domain.tools.OneTimeCode
import net.asere.heightwander.server.shared.core.domain.tools.generateId

@Deprecated("Use SendRecoveryCode instead")
class LegacySendRecoveryCode(
    private val userRepository: UserRepository,
    private val recoveryCodeRepository: RecoveryRepository,
    private val emailService: EmailService,
) {
    suspend operator fun invoke(email: String) {
        val user = userRepository.getByEmail(email) ?: return
        val plaintext = OneTimeCode.generate()
        val code =
            RecoveryCode(
                id = generateId(),
                userId = user.id,
                value = plaintext,
            )
        recoveryCodeRepository.saveCode(code)
        emailService.sendRecoveryCode(code = plaintext, user.email)
    }
}
