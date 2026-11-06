package net.asere.heightwander.server.account.data

import net.asere.heightwander.server.account.data.datasource.UserDataSource
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.User

class UserDataRepository(
    private val dataSource: UserDataSource,
) : UserRepository {
    override suspend fun save(user: User): User = dataSource.save(user)

    override suspend fun getByName(name: String): User? = dataSource.getByName(name)

    override suspend fun getById(id: String): User? = dataSource.getById(id)

    override suspend fun getByIds(ids: List<String>): List<User> = dataSource.getByIds(ids)

    override suspend fun getByEmail(email: String): User? = dataSource.getByEmail(email)

    override suspend fun getByExternalId(externalId: String): User? = dataSource.getByExternalId(externalId)

    override suspend fun listUsers(): List<User> = dataSource.listUsers()

    override suspend fun searchUsers(query: String): List<User> = dataSource.searchUsers(query)

    override suspend fun countUsers(): Long = dataSource.countUsers()

    override suspend fun deleteUser(id: String) = dataSource.deleteUser(id)

    override suspend fun updateUser(user: User) = dataSource.updateUser(user)
}
