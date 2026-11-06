package net.asere.heightwander.server.shared.profile.domain

import net.asere.heightwander.server.shared.profile.domain.model.ProfileShared

interface ProfileService {
    suspend fun createProfile(
        userId: String,
        username: String,
        pictureUrl: String? = null,
    )

    suspend fun updateUsername(
        userId: String,
        username: String,
    )

    suspend fun updatePicture(
        userId: String,
        url: String,
    )

    suspend fun clearPicture(userId: String)

    suspend fun updateProfileDetails(
        userId: String,
        name: String,
        description: String,
    ): UpdateProfileResult

    suspend fun deleteProfile(userId: String)

    suspend fun getProfiles(userIds: List<String>): List<ProfileShared>

    suspend fun getProfile(userId: String): ProfileShared

    suspend fun setSubscribersCount(
        userId: String,
        count: Long,
    )

    suspend fun setSubscriptionsCount(
        userId: String,
        count: Long,
    )

    suspend fun search(query: String): List<ProfileShared>
}
