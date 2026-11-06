package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.InvalidTokenError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class InvalidTokenApiError :
    ApiError(
        code = "InvalidToken",
    ),
    AccountResponse

fun InvalidTokenError.toResponse() = Response<AccountResponse>(
    code = 401,
    body = InvalidTokenApiError(),
)
