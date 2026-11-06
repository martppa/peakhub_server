package net.asere.heightwander.server.account.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.shared.core.domain.service.EmailService
import net.asere.heightwander.server.shared.core.domain.service.LegalUpdateNotificationKind

class NotifyUsersLegalUpdate(
    private val userRepository: UserRepository,
    private val emailService: EmailService,
) {
    data class Result(
        val recipientCount: Int,
        val sentCount: Int,
        val failedCount: Int,
    )

    suspend operator fun invoke(kind: LegalUpdateNotificationKind): Result = withContext(Dispatchers.IO) {
        val emails =
            userRepository
                .listUsers()
                .map { it.email.trim() }
                .filter { it.isNotEmpty() }
                .distinctBy { it.lowercase() }

        var sentCount = 0
        var failedCount = 0
        for (email in emails) {
            if (emailService.sendLegalUpdateNotification(kind = kind, address = email)) {
                sentCount++
            } else {
                failedCount++
            }
        }
        Result(
            recipientCount = emails.size,
            sentCount = sentCount,
            failedCount = failedCount,
        )
    }
}
