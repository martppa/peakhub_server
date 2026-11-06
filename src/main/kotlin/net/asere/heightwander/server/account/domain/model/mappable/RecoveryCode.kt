package net.asere.heightwander.server.account.domain.model.mappable

import java.util.*

data class RecoveryCode(
    val id: String,
    val userId: String,
    val value: String,
    val createdAt: Date = Date(),
) : AccountMappableAggregate
