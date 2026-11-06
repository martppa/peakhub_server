package net.asere.heightwander.server.account.data.datasource

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import java.util.Collections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.asere.heightwander.server.account.domain.model.GoogleUser
import net.asere.heightwander.server.shared.core.domain.environment.Environment

class GoogleOAuthDataSourceImpl(
    environment: Environment,
) : GoogleOAuthDataSource {
    private val verifier =
        GoogleIdTokenVerifier
            .Builder(NetHttpTransport(), GsonFactory())
            .setAudience(Collections.singletonList(environment.getGoogleClientId()))
            .build()

    override suspend fun verifyGoogleToken(idToken: String): GoogleUser? = withContext(Dispatchers.IO) {
        val googleIdToken = verifier.verify(idToken)
        if (googleIdToken != null) {
            val payload = googleIdToken.payload
            GoogleUser(
                userId = payload.subject,
                email = payload.email,
                emailVerified = payload.emailVerified,
                name = payload["name"] as String?,
                pictureUrl = payload["picture"] as String?,
                locale = payload["locale"] as String?,
                familyName = payload["family_name"] as String?,
                givenName = payload["given_name"] as String?,
            )
        } else {
            null
        }
    }
}
