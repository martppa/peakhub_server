package net.asere.peakhub.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable

@Serializable
class NotFoundApiError :
    ApiError(
        code = "NotFound",
    )
