package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.InvalidCredentialsError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class InvalidCredentialsApiError :
    ApiError(
        code = "InvalidCredentials",
    ),
    AccountResponse

fun InvalidCredentialsError.toResponse() = Response<AccountResponse>(
    code = 400,
    body = InvalidCredentialsApiError(),
)
