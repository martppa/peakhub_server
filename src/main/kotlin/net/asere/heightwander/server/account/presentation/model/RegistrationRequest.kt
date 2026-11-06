package net.asere.heightwander.server.account.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isCoach: Boolean = false, // TODO: Remove
)
