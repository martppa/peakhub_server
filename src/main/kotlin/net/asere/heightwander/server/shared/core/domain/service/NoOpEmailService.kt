package net.asere.heightwander.server.shared.core.domain.service

import net.asere.heightwander.server.shared.core.domain.tools.Logger

class NoOpEmailService(
    private val logger: Logger,
) : EmailService {
    override fun sendRecoveryCode(
        code: String,
        address: String,
    ) {
        logger.info("NoOpEmailService: recovery code for $address (not sent)")
    }

    override fun sendVerificationCode(
        code: String,
        address: String,
    ) {
        logger.info("NoOpEmailService: verification code for $address (not sent)")
    }

    override fun sendLegalUpdateNotification(
        kind: LegalUpdateNotificationKind,
        address: String,
    ): Boolean {
        logger.info("NoOpEmailService: legal update $kind for $address (not sent)")
        return true
    }
}
