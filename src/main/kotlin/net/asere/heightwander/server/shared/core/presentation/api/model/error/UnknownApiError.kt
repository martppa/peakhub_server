package net.asere.heightwander.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response

@Serializable
class UnknownApiError :
    ApiError(
        code = "Unknown",
    )

fun UnknownApiError.toResponse() =
    Response<Any>(
        code = 500,
        body = this,
    )
