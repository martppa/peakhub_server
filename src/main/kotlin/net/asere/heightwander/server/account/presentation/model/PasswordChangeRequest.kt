package net.asere.heightwander.server.account.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeRequest(
    val currentPassword: String? = null,
    val newPassword: String? = null,
)

fun PasswordChangeRequest.isEmpty() = currentPassword.isNullOrEmpty() || newPassword.isNullOrEmpty()
