package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.InvalidEmailError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class InvalidEmailApiError :
    ApiError(
        code = "InvalidEmail",
    ),
    AccountResponse

fun InvalidEmailError.toResponse() = Response<AccountResponse>(
    code = 400,
    body = InvalidEmailApiError(),
)
