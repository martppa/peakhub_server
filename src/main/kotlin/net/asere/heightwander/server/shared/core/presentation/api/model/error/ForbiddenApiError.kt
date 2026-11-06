package net.asere.heightwander.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response

@Serializable
class ForbiddenApiError :
    ApiError(
        code = "Forbidden",
    )

fun ForbiddenApiError.toResponse() =
    Response<Any>(
        code = 403,
        body = this,
    )
