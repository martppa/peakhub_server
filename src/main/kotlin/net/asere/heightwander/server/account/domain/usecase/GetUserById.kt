package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.shared.account.domain.model.error.UserDoesNotExistError

class GetUserById(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(id: String): User = repository.getById(id) ?: throw UserDoesNotExistError
}
