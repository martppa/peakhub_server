package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.CypherInterface
import net.asere.heightwander.server.account.domain.model.mappable.InvalidCredentialsError
import net.asere.heightwander.server.shared.account.domain.model.error.UserDoesNotExistError
import net.asere.heightwander.server.shared.core.domain.model.error.BadRequestError

class ChangePassword(
    private val userRepository: UserRepository,
    private val cypherInterface: CypherInterface,
    private val updateUserPassword: UpdateUserPassword,
) {
    suspend operator fun invoke(
        userId: String,
        currentPassword: String,
        newPassword: String,
    ) {
        val user = userRepository.getById(userId) ?: throw UserDoesNotExistError
        if (!cypherInterface.authenticate(
                currentPassword,
                user.password ?: throw BadRequestError("You can't change the password of a social user"),
            )
        ) {
            throw InvalidCredentialsError
        }
        updateUserPassword(userId, newPassword)
    }
}
