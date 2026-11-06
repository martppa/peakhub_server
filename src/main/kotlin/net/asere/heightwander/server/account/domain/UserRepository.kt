package net.asere.heightwander.server.account.domain

import net.asere.heightwander.server.account.domain.model.User

interface UserRepository {
    suspend fun save(user: User): User

    suspend fun getByName(name: String): User?

    suspend fun getById(id: String): User?

    suspend fun getByIds(ids: List<String>): List<User>

    suspend fun getByEmail(email: String): User?

    suspend fun getByExternalId(externalId: String): User?

    suspend fun listUsers(): List<User>

    suspend fun searchUsers(query: String): List<User>

    suspend fun countUsers(): Long

    suspend fun deleteUser(id: String)

    suspend fun updateUser(user: User)
}
