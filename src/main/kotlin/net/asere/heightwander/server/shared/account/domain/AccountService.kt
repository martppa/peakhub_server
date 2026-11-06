package net.asere.heightwander.server.shared.account.domain

import net.asere.heightwander.server.shared.account.domain.model.UserShared
import net.asere.heightwander.server.shared.account.domain.model.UserUpdateShared
import net.asere.heightwander.server.shared.core.domain.service.LegalUpdateNotificationKind

interface AccountService {
    suspend fun getUser(userId: String): UserShared

    suspend fun getUsers(userIds: List<String>): List<UserShared>

    suspend fun getUserByName(username: String): UserShared

    suspend fun doesUserExist(userId: String): Boolean

    suspend fun searchUsers(query: String): List<UserShared>

    suspend fun listUsers(): List<UserShared>

    suspend fun countUsers(): Long

    suspend fun updateUser(update: UserUpdateShared): UpdateUserResult

    suspend fun deleteUser(userId: String): DeleteUserResult

    suspend fun notifyLegalUpdate(kind: LegalUpdateNotificationKind): NotifyLegalUpdateResult
}
