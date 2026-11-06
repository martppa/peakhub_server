package net.asere.heightwander.server.shared.profile.domain.model

data class ProfileShared(
    val id: String = "",
    val userId: String,
    val username: String,
    val name: String,
    val description: String = "",
    val pictureUrl: String? = null,
    val attributes: ProfileAttributesShared = ProfileAttributesShared(),
    val subscribers: Long = 0,
    val subscriptions: Long = 0,
    val subscribed: Boolean = false,
)
