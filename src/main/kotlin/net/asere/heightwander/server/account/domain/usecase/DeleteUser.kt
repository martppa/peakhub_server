package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.shared.profile.domain.ProfileService
import net.asere.heightwander.server.shared.storage.domain.StorageService
import net.asere.heightwander.server.shared.subscription.domain.SubscriptionService
import net.asere.heightwander.server.shared.trail.domain.TrailService

class DeleteUser(
    private val userRepository: UserRepository,
    private val profileService: ProfileService,
    private val storageService: StorageService,
    private val trailService: TrailService,
    private val subscriptionService: SubscriptionService,
) {
    suspend operator fun invoke(id: String) {
        subscriptionService.deleteUserSubscriptions(id)
        trailService.deleteUserTrails(id)
        profileService.deleteProfile(id)
        storageService.deleteMediasByUserId(id)
        userRepository.deleteUser(id)
    }
}
