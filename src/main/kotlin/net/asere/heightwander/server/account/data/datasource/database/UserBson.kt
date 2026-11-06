package net.asere.heightwander.server.account.data.datasource.database

import java.util.*
import net.asere.heightwander.server.account.domain.model.User
import org.bson.codecs.pojo.annotations.BsonId

data class UserBson(
    @param:BsonId
    var id: String? = null,
    var externalId: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var type: Type? = null,
    var source: Source? = null,
    var emailVerified: Boolean? = null,
    var createdAt: Date? = null,
    var tokenVersion: Int? = null,
) {
    enum class Type {
        Customer,
        Moderator,
        Creator,
        Admin,
        Tester,
    }

    enum class Source {
        Email,
        Google,
        Apple,
    }
}

fun User.Type.asBson() = when (this) {
    User.Type.Customer -> UserBson.Type.Customer
    User.Type.Admin -> UserBson.Type.Admin
    User.Type.Moderator -> UserBson.Type.Moderator
    User.Type.Creator -> UserBson.Type.Creator
    User.Type.Tester -> UserBson.Type.Tester
}

fun User.Source.asBson() = when (this) {
    User.Source.Email -> UserBson.Source.Email
    User.Source.Google -> UserBson.Source.Google
    User.Source.Apple -> UserBson.Source.Apple
}

fun User.asBson() = UserBson(
    id = id,
    externalId = externalId,
    name = name,
    email = email,
    password = password,
    type = type.asBson(),
    source = source.asBson(),
    emailVerified = emailVerified,
    createdAt = createdAt?.let { Date(it) },
    tokenVersion = tokenVersion,
)

fun UserBson.Type.asDomain() = when (this) {
    UserBson.Type.Customer -> User.Type.Customer
    UserBson.Type.Admin -> User.Type.Admin
    UserBson.Type.Moderator -> User.Type.Moderator
    UserBson.Type.Creator -> User.Type.Creator
    UserBson.Type.Tester -> User.Type.Tester
}

fun UserBson.Source.asDomain() = when (this) {
    UserBson.Source.Email -> User.Source.Email
    UserBson.Source.Google -> User.Source.Google
    UserBson.Source.Apple -> User.Source.Apple
}

fun UserBson.asDomain() = User(
    id = id!!,
    externalId = externalId,
    name = name!!,
    email = email!!,
    password = password,
    type = type!!.asDomain(),
    source = source!!.asDomain(),
    emailVerified = emailVerified ?: false,
    createdAt = createdAt?.time,
    tokenVersion = tokenVersion ?: 0,
)
