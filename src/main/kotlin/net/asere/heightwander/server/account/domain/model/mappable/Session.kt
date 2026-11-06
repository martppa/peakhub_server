package net.asere.heightwander.server.account.domain.model.mappable

import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.account.presentation.model.asApi

data class Session(
    val userId: String,
    val token: String,
    val refresh: String,
) : AccountMappableAggregate

fun Session.toResponse(): Response<AccountResponse> = Response(
    body = asApi(),
)
