package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.model.mappable.UserAlreadyExistError
import net.asere.heightwander.server.shared.account.domain.model.error.UserDoesNotExistError
import net.asere.heightwander.server.shared.profile.domain.ProfileService

class UpdateUser(
    private val userRepository: UserRepository,
    private val profileService: ProfileService,
) {
    suspend operator fun invoke(user: User) {
        val storedUser = userRepository.getById(user.id) ?: throw UserDoesNotExistError
        val userWithEmail = userRepository.getByEmail(user.email)
        val userWithName = userRepository.getByName(user.name)
        if ((userWithEmail?.email != storedUser.email && userWithEmail?.email != null) ||
            (userWithName?.name != storedUser.name && userWithName?.name != null)
        ) {
            throw UserAlreadyExistError
        }
        val editedUser =
            storedUser.copy(
                name = user.name,
                email = user.email,
            )
        userRepository.updateUser(editedUser)
        if (user.name != storedUser.name) {
            profileService.updateUsername(
                userId = editedUser.id,
                username = editedUser.name,
            )
        }
    }
}
