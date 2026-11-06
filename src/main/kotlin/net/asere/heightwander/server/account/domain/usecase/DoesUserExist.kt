package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository

class DoesUserExist(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(userId: String): Boolean = repository.getById(userId) != null
}
