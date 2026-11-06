package net.asere.heightwander.server.account.domain.model.mappable

import java.util.*

data class VerificationCode(
    val id: String,
    val userId: String,
    val value: String,
    val createdAt: Date = Date(),
)
