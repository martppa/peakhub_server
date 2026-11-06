package net.asere.heightwander.server.account.presentation.model

import kotlinx.serialization.Serializable
import net.asere.heightwander.server.account.domain.model.User

@Serializable
data class UserModel(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val type: Type? = null,
) {
    enum class Type {
        Customer,
        Moderator,
        Creator,
        Admin,
        Tester,
    }
}

fun UserModel.asDomain() = User(
    id = id.orEmpty(),
    name = name!!,
    email = email!!,
    password = password.orEmpty(),
    type = type?.asDomain() ?: User.Type.Customer,
    source = User.Source.Email,
)

fun User.asApi() = UserModel(
    id = id,
    name = name,
    email = email,
    type = type.asApi(),
)

fun User.Type.asApi() = when (this) {
    User.Type.Customer -> UserModel.Type.Customer
    User.Type.Admin -> UserModel.Type.Admin
    User.Type.Moderator -> UserModel.Type.Moderator
    User.Type.Creator -> UserModel.Type.Creator
    User.Type.Tester -> UserModel.Type.Tester
}

fun UserModel.Type.asDomain() = when (this) {
    UserModel.Type.Customer -> User.Type.Customer
    UserModel.Type.Admin -> User.Type.Admin
    UserModel.Type.Moderator -> User.Type.Moderator
    UserModel.Type.Creator -> User.Type.Creator
    UserModel.Type.Tester -> User.Type.Tester
}

fun User.update(user: UserModel) = copy(
    name = user.name ?: name,
    email = user.email ?: email,
)
