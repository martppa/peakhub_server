package net.asere.heightwander.server.account.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenModel(
    val token: String,
)
