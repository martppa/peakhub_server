package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.AccessDisabledError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class AccessDisabledApiError :
    ApiError(
        code = "AccessDisabled",
    ),
    AccountResponse

fun AccessDisabledError.toResponse() = Response<AccountResponse>(
    code = 503,
    body = AccessDisabledApiError(),
)
