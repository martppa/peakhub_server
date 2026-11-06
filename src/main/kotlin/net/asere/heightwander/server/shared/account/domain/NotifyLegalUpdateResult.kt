package net.asere.heightwander.server.shared.account.domain

data class NotifyLegalUpdateResult(
    val recipientCount: Int,
    val sentCount: Int,
    val failedCount: Int,
)
