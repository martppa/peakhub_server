package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.InvalidPasswordError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class InvalidPasswordApiError :
    ApiError(
        code = "InvalidPassword",
    ),
    AccountResponse

fun InvalidPasswordError.toResponse() = Response<AccountResponse>(
    code = 400,
    body = InvalidPasswordApiError(),
)
