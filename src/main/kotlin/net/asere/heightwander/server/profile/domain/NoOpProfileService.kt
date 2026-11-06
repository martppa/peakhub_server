package net.asere.heightwander.server.profile.domain

import net.asere.heightwander.server.shared.core.domain.tools.Logger
import net.asere.heightwander.server.shared.core.domain.tools.generateId
import net.asere.heightwander.server.shared.profile.domain.ProfileService
import net.asere.heightwander.server.shared.profile.domain.UpdateProfileResult
import net.asere.heightwander.server.shared.profile.domain.model.ProfileShared

class NoOpProfileService(
    private val logger: Logger,
) : ProfileService {
    private val profiles = mutableMapOf<String, ProfileShared>()

    override suspend fun createProfile(
        userId: String,
        username: String,
        pictureUrl: String?,
    ) {
        profiles[userId] =
            ProfileShared(
                id = generateId(),
                userId = userId,
                username = username,
                name = username,
                pictureUrl = pictureUrl,
            )
        logger.debug("NoOpProfileService: created profile for $userId")
    }

    override suspend fun updateUsername(
        userId: String,
        username: String,
    ) {
        profiles[userId]?.let { profiles[userId] = it.copy(username = username, name = username) }
    }

    override suspend fun updatePicture(
        userId: String,
        url: String,
    ) {
        profiles[userId]?.let { profiles[userId] = it.copy(pictureUrl = url) }
    }

    override suspend fun clearPicture(userId: String) {
        profiles[userId]?.let { profiles[userId] = it.copy(pictureUrl = null) }
    }

    override suspend fun updateProfileDetails(
        userId: String,
        name: String,
        description: String,
    ): UpdateProfileResult {
        val existing = profiles[userId] ?: return UpdateProfileResult.NotFound
        profiles[userId] = existing.copy(name = name, description = description)
        return UpdateProfileResult.Success
    }

    override suspend fun deleteProfile(userId: String) {
        profiles.remove(userId)
    }

    override suspend fun getProfiles(userIds: List<String>): List<ProfileShared> =
        userIds.mapNotNull { profiles[it] }

    override suspend fun getProfile(userId: String): ProfileShared =
        profiles[userId]
            ?: ProfileShared(userId = userId, username = userId, name = userId)

    override suspend fun setSubscribersCount(
        userId: String,
        count: Long,
    ) {
        profiles[userId]?.let { profiles[userId] = it.copy(subscribers = count) }
    }

    override suspend fun setSubscriptionsCount(
        userId: String,
        count: Long,
    ) {
        profiles[userId]?.let { profiles[userId] = it.copy(subscriptions = count) }
    }

    override suspend fun search(query: String): List<ProfileShared> =
        profiles.values.filter {
            it.username.contains(query, ignoreCase = true) ||
                it.name.contains(query, ignoreCase = true)
        }
}
