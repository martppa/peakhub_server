package net.asere.heightwander.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable
import net.asere.heightwander.server.shared.core.domain.model.error.BadRequestError
import net.asere.omni.ktor.Response

@Serializable
class BadRequestApiError(
    val message: String,
) : ApiError(
        code = "BadRequest",
    )

fun BadRequestApiError.toResponse() =
    Response<Any>(
        code = 400,
        body = this,
    )

fun BadRequestError.toApi() = BadRequestApiError(message)
