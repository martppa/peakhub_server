package net.asere.heightwander.server.account.presentation.api

import io.ktor.server.application.createRouteScopedPlugin
import net.asere.heightwander.server.account.domain.model.asShared
import net.asere.heightwander.server.shared.core.presentation.api.auth.AuthTokenResolver
import net.asere.heightwander.server.shared.core.presentation.common.UserContext
import net.asere.heightwander.server.shared.core.presentation.common.extension.userContext
import net.asere.heightwander.server.shared.core.presentation.web.session.WebSessionKeys

const val USER_DATA_PLUGIN_NAME = WebSessionKeys.USER_DATA_PLUGIN

fun createUserDataPlugin(authTokenResolver: AuthTokenResolver) =
    createRouteScopedPlugin(name = WebSessionKeys.USER_DATA_PLUGIN) {
        onCall { call ->
            val token = authTokenResolver.resolveAccessToken(call) ?: return@onCall
            val language = call.request.headers[WebSessionKeys.CLIENT_LANGUAGE_HEADER]
            call.userContext =
                UserContext(
                    id = token.userId,
                    type = token.userType.asShared(),
                    language = language,
                )
        }
    }
