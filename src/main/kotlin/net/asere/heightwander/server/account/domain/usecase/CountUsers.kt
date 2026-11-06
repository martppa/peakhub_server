package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository

class CountUsers(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Long = userRepository.countUsers()
}
