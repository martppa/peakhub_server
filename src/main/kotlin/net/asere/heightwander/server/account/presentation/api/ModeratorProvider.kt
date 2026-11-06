package net.asere.heightwander.server.account.presentation.api

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.usecase.GetUserById
import net.asere.heightwander.server.shared.account.api.AuthorizationScopes
import net.asere.heightwander.server.shared.account.domain.model.UserShared
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ForbiddenApiError
import net.asere.heightwander.server.shared.core.presentation.common.extension.userId
import net.asere.heightwander.server.shared.core.presentation.common.extension.userType
import org.koin.core.context.GlobalContext

fun AuthenticationConfig.moderatorProvider() {
    provider(AuthorizationScopes.MODERATOR_SCOPE) {
        authenticate { context ->
            val jwtOk =
                context.call.userType == UserShared.Type.Admin ||
                    context.call.userType == UserShared.Type.Moderator
            val allowed =
                jwtOk &&
                    context.call.userId?.let { uid ->
                        runBlocking {
                            runCatching {
                                val type = GlobalContext.get().get<GetUserById>()(uid).type
                                type == User.Type.Admin || type == User.Type.Moderator
                            }.getOrDefault(false)
                        }
                    } == true
            if (!allowed) {
                context.challenge(
                    "ModeratorVerification",
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
