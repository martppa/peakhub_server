package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.UserDoesNotExistError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class UserDoesNotExistApiError :
    ApiError(
        code = "UserDoesNotExist",
    ),
    AccountResponse

fun UserDoesNotExistError.toResponse() = Response<AccountResponse>(
    code = 404,
    body = UserDoesNotExistApiError(),
)
