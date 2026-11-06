package net.asere.heightwander.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable

@Serializable
class UnknownApiError :
    ApiError(
        code = "Unknown",
    )
