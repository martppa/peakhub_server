package net.asere.heightwander.server.shared.core.domain.service

interface EmailService {
    fun sendRecoveryCode(
        code: String,
        address: String,
    )

    fun sendVerificationCode(
        code: String,
        address: String,
    )

    fun sendLegalUpdateNotification(
        kind: LegalUpdateNotificationKind,
        address: String,
    ): Boolean
}
