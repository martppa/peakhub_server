package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class AccountUnknownApiError :
    ApiError(
        code = "UnknownError",
    ),
    AccountResponse

fun AccountUnknownApiError.toResponse() = Response<AccountResponse>(
    code = 500,
    body = AccountUnknownApiError(),
)
