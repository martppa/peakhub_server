package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.WrongSourceError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
object WrongSourceApiError :
    ApiError(
        code = "WrongSource",
    ),
    AccountResponse

fun WrongSourceError.toResponse() = Response<AccountResponse>(
    code = 401,
    body = WrongSourceApiError,
)
