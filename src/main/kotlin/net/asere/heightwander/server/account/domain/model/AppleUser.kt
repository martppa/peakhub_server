package net.asere.heightwander.server.account.domain.model

data class AppleUser(
    val userId: String,
    val email: String?,
    val emailVerified: Boolean,
    val name: String?,
) : AccountAggregate
