package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.InvalidVerificationCodeError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class InvalidVerificationCodeApiError :
    ApiError(
        code = "InvalidVerificationCode",
    ),
    AccountResponse

fun InvalidVerificationCodeError.toResponse() = Response<AccountResponse>(
    code = 400,
    body = InvalidVerificationCodeApiError(),
)
