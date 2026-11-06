package net.asere.heightwander.server.account.data

import net.asere.heightwander.server.account.data.datasource.AppleOAuthDataSource
import net.asere.heightwander.server.account.data.datasource.GoogleOAuthDataSource
import net.asere.heightwander.server.account.domain.OAuthRepository
import net.asere.heightwander.server.account.domain.model.AppleUser
import net.asere.heightwander.server.account.domain.model.GoogleUser

class OAuthDataRepository(
    private val googleOAuthDataSource: GoogleOAuthDataSource,
    private val appleOAuthDataSource: AppleOAuthDataSource,
) : OAuthRepository {
    override suspend fun verifyGoogleToken(idToken: String): GoogleUser? = googleOAuthDataSource.verifyGoogleToken(idToken)

    override suspend fun verifyAppleToken(idToken: String): AppleUser? = appleOAuthDataSource.verifyAppleToken(idToken)
}
