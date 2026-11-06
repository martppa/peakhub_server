package net.asere.heightwander.server.account.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class RecoveryRequest(
    val code: String = "",
    val password: String = "",
)
