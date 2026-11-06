package net.asere.heightwander.server.account.domain

import net.asere.heightwander.server.account.domain.model.AppleUser
import net.asere.heightwander.server.account.domain.model.GoogleUser

interface OAuthRepository {
    suspend fun verifyGoogleToken(idToken: String): GoogleUser?

    suspend fun verifyAppleToken(idToken: String): AppleUser?
}
