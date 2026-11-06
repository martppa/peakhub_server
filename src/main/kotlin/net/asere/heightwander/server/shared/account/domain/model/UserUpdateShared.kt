package net.asere.heightwander.server.shared.account.domain.model

data class UserUpdateShared(
    val id: String,
    val name: String,
    val email: String,
    val type: UserShared.Type,
    val password: String? = null,
    val emailVerified: Boolean = false,
)
