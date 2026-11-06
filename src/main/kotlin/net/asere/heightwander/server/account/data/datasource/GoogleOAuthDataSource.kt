package net.asere.heightwander.server.account.data.datasource

import net.asere.heightwander.server.account.domain.model.GoogleUser

interface GoogleOAuthDataSource {
    suspend fun verifyGoogleToken(idToken: String): GoogleUser?
}
