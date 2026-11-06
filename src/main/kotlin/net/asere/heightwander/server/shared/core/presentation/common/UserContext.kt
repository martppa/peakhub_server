package net.asere.heightwander.server.shared.core.presentation.common

import net.asere.heightwander.server.shared.account.domain.model.UserShared
import net.asere.heightwander.server.shared.core.domain.model.error.ForbiddenError

data class UserContext(
    val id: String,
    val type: UserShared.Type,
    val language: String?,
)

val UserContext.isCreator: Boolean get() = type == UserShared.Type.Admin || type == UserShared.Type.Creator
val UserContext.isAdmin: Boolean get() = type == UserShared.Type.Admin
val UserContext.isCustomer: Boolean get() = type == UserShared.Type.Customer

fun UserContext.verify(userId: String) {
    if (id != userId && type != UserShared.Type.Admin) throw ForbiddenError
}
