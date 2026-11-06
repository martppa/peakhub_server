package net.asere.heightwander.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response

@Serializable
class UnauthorizedApiError :
    ApiError(
        code = "Unauthorized",
    )

fun UnauthorizedApiError.toResponse() =
    Response<Any>(
        code = 401,
        body = this,
    )
