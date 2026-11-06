package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.CypherInterface
import net.asere.heightwander.server.account.domain.model.User

class AdminOnlyCreateUser(
    private val cypherInterface: CypherInterface,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(user: User) {
        val hashedUser =
            user.copy(
                password = cypherInterface.hash(user.password!!),
                emailVerified = true,
            )
        userRepository.save(hashedUser)
    }
}
