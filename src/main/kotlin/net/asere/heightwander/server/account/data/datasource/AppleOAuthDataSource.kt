package net.asere.heightwander.server.account.data.datasource

import net.asere.heightwander.server.account.domain.model.AppleUser

interface AppleOAuthDataSource {
    suspend fun verifyAppleToken(idToken: String): AppleUser?
}
