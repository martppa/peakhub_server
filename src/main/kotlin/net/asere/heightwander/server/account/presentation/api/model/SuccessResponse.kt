package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.Success
import net.asere.heightwander.server.account.presentation.api.AccountResponse

@Serializable
class SuccessResponse : AccountResponse

fun Success.toResponse(): Response<AccountResponse> = Response(
    body = SuccessResponse(),
)
