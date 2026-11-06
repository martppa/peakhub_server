package net.asere.heightwander.server.account.presentation.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.*
import io.ktor.server.response.respond
import kotlinx.coroutines.runBlocking
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.usecase.GetUserById
import net.asere.heightwander.server.shared.account.api.AuthorizationScopes
import net.asere.heightwander.server.shared.account.domain.model.UserShared
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ForbiddenApiError
import net.asere.heightwander.server.shared.core.presentation.common.extension.userId
import net.asere.heightwander.server.shared.core.presentation.common.extension.userType
import org.koin.core.context.GlobalContext

fun AuthenticationConfig.adminProvider() {
    provider(AuthorizationScopes.ADMIN_SCOPE) {
        authenticate { context ->
            val allowed =
                context.call.userType == UserShared.Type.Admin &&
                    context.call.userId?.let { uid ->
                        runBlocking {
                            runCatching {
                                GlobalContext.get().get<GetUserById>()(uid).type == User.Type.Admin
                            }.getOrDefault(false)
                        }
                    } == true
            if (!allowed) {
                context.challenge(
                    "AdminVerification",
                    AuthenticationFailedCause.Error("Forbidden"),
                ) { challenge, call ->
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = ForbiddenApiError(),
                    )
                    challenge.complete()
                }
            }
        }
    }
}
