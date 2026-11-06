package net.asere.heightwander.server.account.domain.model

import net.asere.heightwander.server.shared.account.domain.model.UserShared

data class User(
    val id: String,
    val externalId: String? = null,
    val name: String,
    val email: String,
    val password: String? = null,
    val type: Type,
    val source: Source,
    val emailVerified: Boolean = false,
    val createdAt: Long? = null,
    /** Bumped on password change to invalidate outstanding JWTs. */
    val tokenVersion: Int = 0,
) : AccountAggregate {
    enum class Source {
        Email,
        Google,
        Apple,
    }

    enum class Type {
        Customer,
        Moderator,
        Creator,
        Admin,
        Tester,
    }
}

fun User.asShared() = UserShared(
    id = id,
    name = name,
    email = email,
    type = type.asShared(),
    source = source.asShared(),
    emailVerified = emailVerified,
    createdAt = createdAt,
)

fun User.Type.asShared() = when (this) {
    User.Type.Admin -> UserShared.Type.Admin
    User.Type.Customer -> UserShared.Type.Customer
    User.Type.Moderator -> UserShared.Type.Moderator
    User.Type.Creator -> UserShared.Type.Creator
    User.Type.Tester -> UserShared.Type.Tester
}

fun User.Source.asShared() = when (this) {
    User.Source.Email -> UserShared.Source.Email
    User.Source.Google -> UserShared.Source.Google
    User.Source.Apple -> UserShared.Source.Apple
}

fun UserShared.Type.asDomain() = when (this) {
    UserShared.Type.Admin -> User.Type.Admin
    UserShared.Type.Customer -> User.Type.Customer
    UserShared.Type.Moderator -> User.Type.Moderator
    UserShared.Type.Creator -> User.Type.Creator
    UserShared.Type.Tester -> User.Type.Tester
}

val User.Type.canAccessWhenPublicAccessDisabled: Boolean
    get() = this == User.Type.Admin || this == User.Type.Tester
