package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.User

class GetUsersByIds(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(ids: List<String>): List<User> = repository.getByIds(ids)
}
