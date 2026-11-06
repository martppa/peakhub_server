package net.asere.heightwander.server.main.server.ktor.auth

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.AuthenticationFailedCause
import io.ktor.server.auth.*
import io.ktor.server.response.respond
import net.asere.heightwander.server.shared.account.api.AuthorizationScopes
import net.asere.heightwander.server.shared.core.presentation.api.model.error.UnauthorizedApiError
import net.asere.heightwander.server.shared.core.presentation.common.extension.userContext

fun AuthenticationConfig.loggedProvider() {
    provider(AuthorizationScopes.API_LOGGED_SCOPE) {
        authenticate { context ->
            if (context.call.userContext == null) {
                context.challenge(
                    "LoginVerification",
                    AuthenticationFailedCause.Error("Unauthorized"),
                ) { challenge, call ->
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = UnauthorizedApiError(),
                    )
                    challenge.complete()
                }
            }
        }
    }
}
