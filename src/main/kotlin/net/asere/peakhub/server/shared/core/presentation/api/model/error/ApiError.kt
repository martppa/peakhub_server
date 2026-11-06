package net.asere.peakhub.server.shared.core.presentation.api.model.error

import kotlinx.serialization.Serializable

@Serializable
open class ApiError(
    val code: String,
)
