package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.UserAlreadyExistError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class UserAlreadyExistApiError :
    ApiError(
        code = "UserAlreadyExist",
    ),
    AccountResponse

fun UserAlreadyExistError.toResponse() = Response<AccountResponse>(
    code = 409,
    body = UserAlreadyExistApiError(),
)
