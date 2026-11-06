package net.asere.heightwander.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable
import net.asere.heightwander.server.shared.core.domain.model.error.BadRequestError

@Serializable
class BadRequestApiError(
    val message: String,
) : ApiError(
        code = "BadRequest",
    )

fun BadRequestError.toApi() = BadRequestApiError(message)
