package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.User

class SearchUsers(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(query: String): List<User> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return emptyList()
        return userRepository.searchUsers(trimmed)
    }
}
