package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.VerificationRepository
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.model.mappable.VerificationCode
import net.asere.heightwander.server.shared.core.domain.service.EmailService
import net.asere.heightwander.server.shared.core.domain.tools.OneTimeCode
import net.asere.heightwander.server.shared.core.domain.tools.generateId

class SendVerificationEmail(
    private val verificationRepository: VerificationRepository,
    private val emailService: EmailService,
) {
    suspend operator fun invoke(user: User) {
        if (user.emailVerified) return
        val plaintext = OneTimeCode.generate()
        val code =
            VerificationCode(
                id = generateId(),
                userId = user.id,
                value = plaintext,
            )
        verificationRepository.saveCode(code)
        emailService.sendVerificationCode(code = plaintext, address = user.email)
    }
}
