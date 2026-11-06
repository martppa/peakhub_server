package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.CypherInterface
import net.asere.heightwander.server.shared.account.domain.model.error.UserDoesNotExistError

class UpdateUserPassword(
    private val cypherInterface: CypherInterface,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        userId: String,
        password: String,
    ) {
        val storedUser = userRepository.getById(userId) ?: throw UserDoesNotExistError
        val hashedPassword = cypherInterface.hash(password)
        val editedUser =
            storedUser.copy(
                password = hashedPassword,
                tokenVersion = storedUser.tokenVersion + 1,
            )
        userRepository.updateUser(editedUser)
    }
}
