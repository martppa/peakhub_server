package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.User

class AdminOnlyListUsers(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): List<User> = userRepository.listUsers()
}
