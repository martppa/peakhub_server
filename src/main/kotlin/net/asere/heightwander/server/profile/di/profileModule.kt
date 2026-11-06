package net.asere.heightwander.server.profile.di

import net.asere.heightwander.server.profile.domain.NoOpProfileService
import net.asere.heightwander.server.shared.profile.domain.ProfileService
import net.asere.heightwander.server.shared.storage.domain.StorageService
import net.asere.heightwander.server.shared.subscription.domain.SubscriptionService
import net.asere.heightwander.server.shared.trail.domain.TrailService
import org.koin.dsl.bind
import org.koin.dsl.module

val profileModule =
    module {
        single { NoOpProfileService(get()) } bind ProfileService::class
        single {
            object : StorageService {
                override suspend fun deleteMediasByUserId(userId: String) = Unit
            }
        } bind StorageService::class
        single {
            object : TrailService {
                override suspend fun deleteUserTrails(userId: String) = Unit
            }
        } bind TrailService::class
        single {
            object : SubscriptionService {
                override suspend fun deleteUserSubscriptions(userId: String) = Unit
            }
        } bind SubscriptionService::class
    }
