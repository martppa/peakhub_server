package net.asere.heightwander.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response

@Serializable
class NotFoundApiError :
    ApiError(
        code = "NotFound",
    )

fun NotFoundApiError.toResponse() =
    Response<Any>(
        code = 404,
        body = this,
    )
