package net.asere.heightwander.server.account.domain

import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.model.asDomain
import net.asere.heightwander.server.account.domain.model.asShared
import net.asere.heightwander.server.account.domain.usecase.AdminOnlyListUsers
import net.asere.heightwander.server.account.domain.usecase.AdminUpdateUser
import net.asere.heightwander.server.account.domain.usecase.CountUsers
import net.asere.heightwander.server.account.domain.usecase.DeleteUser
import net.asere.heightwander.server.account.domain.usecase.DoesUserExist
import net.asere.heightwander.server.account.domain.usecase.GetUserById
import net.asere.heightwander.server.account.domain.usecase.GetUserByName
import net.asere.heightwander.server.account.domain.usecase.GetUsersByIds
import net.asere.heightwander.server.account.domain.usecase.NotifyUsersLegalUpdate
import net.asere.heightwander.server.account.domain.usecase.SearchUsers
import net.asere.heightwander.server.shared.account.domain.AccountService
import net.asere.heightwander.server.shared.account.domain.DeleteUserResult
import net.asere.heightwander.server.shared.account.domain.NotifyLegalUpdateResult
import net.asere.heightwander.server.shared.account.domain.UpdateUserResult
import net.asere.heightwander.server.shared.account.domain.model.UserShared
import net.asere.heightwander.server.shared.account.domain.model.UserUpdateShared
import net.asere.heightwander.server.shared.core.domain.service.LegalUpdateNotificationKind

class AccountProxy(
    private val getUsersByIdsCase: GetUsersByIds,
    private val getUserByIdCase: GetUserById,
    private val getUserByNameCase: GetUserByName,
    private val doesUserExistCase: DoesUserExist,
    private val searchUsersCase: SearchUsers,
    private val adminOnlyListUsersCase: AdminOnlyListUsers,
    private val countUsersCase: CountUsers,
    private val adminUpdateUserCase: AdminUpdateUser,
    private val deleteUserCase: DeleteUser,
    private val notifyUsersLegalUpdateCase: NotifyUsersLegalUpdate,
) : AccountService {
    override suspend fun getUser(userId: String): UserShared = getUserByIdCase(userId).asShared()

    override suspend fun getUsers(userIds: List<String>): List<UserShared> = getUsersByIdsCase(userIds).map { it.asShared() }

    override suspend fun getUserByName(username: String): UserShared = getUserByNameCase(username).asShared()

    override suspend fun doesUserExist(userId: String): Boolean = doesUserExistCase(userId)

    override suspend fun searchUsers(query: String): List<UserShared> = searchUsersCase(query).map { it.asShared() }

    override suspend fun listUsers(): List<UserShared> = adminOnlyListUsersCase().map { it.asShared() }

    override suspend fun countUsers(): Long = countUsersCase()

    override suspend fun updateUser(update: UserUpdateShared): UpdateUserResult = when (
        adminUpdateUserCase(
            User(
                id = update.id,
                name = update.name,
                email = update.email,
                type = update.type.asDomain(),
                password = update.password,
                source = User.Source.Email,
                emailVerified = update.emailVerified,
            ),
        )
    ) {
        AdminUpdateUser.Result.Success -> UpdateUserResult.Success
        AdminUpdateUser.Result.NotFound -> UpdateUserResult.NotFound
        AdminUpdateUser.Result.AlreadyExists -> UpdateUserResult.AlreadyExists
        AdminUpdateUser.Result.InvalidUsername -> UpdateUserResult.InvalidUsername
        AdminUpdateUser.Result.InvalidEmail -> UpdateUserResult.InvalidEmail
        AdminUpdateUser.Result.InvalidPassword -> UpdateUserResult.InvalidPassword
    }

    override suspend fun deleteUser(userId: String): DeleteUserResult {
        if (!doesUserExistCase(userId)) return DeleteUserResult.NotFound
        deleteUserCase(userId)
        return DeleteUserResult.Success
    }

    override suspend fun notifyLegalUpdate(kind: LegalUpdateNotificationKind): NotifyLegalUpdateResult {
        val result = notifyUsersLegalUpdateCase(kind)
        return NotifyLegalUpdateResult(
            recipientCount = result.recipientCount,
            sentCount = result.sentCount,
            failedCount = result.failedCount,
        )
    }
}
