package net.asere.heightwander.server.account.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val returnTo: String? = null,
)
