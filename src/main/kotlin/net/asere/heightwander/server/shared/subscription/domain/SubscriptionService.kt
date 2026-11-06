package net.asere.heightwander.server.shared.subscription.domain

interface SubscriptionService {
    suspend fun deleteUserSubscriptions(userId: String)
}
