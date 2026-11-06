package net.asere.heightwander.server.account.domain.model

data class GoogleUser(
    val userId: String,
    val email: String,
    val emailVerified: Boolean,
    val name: String?,
    val pictureUrl: String?,
    val locale: String?,
    val familyName: String?,
    val givenName: String?,
) : AccountAggregate
