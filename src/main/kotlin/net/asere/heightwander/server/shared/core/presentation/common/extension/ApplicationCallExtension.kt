package net.asere.heightwander.server.shared.core.presentation.common.extension

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.path
import net.asere.heightwander.server.shared.account.domain.model.UserShared
import net.asere.heightwander.server.shared.core.presentation.common.UserContext
import net.asere.heightwander.server.shared.core.presentation.web.session.CallAttributes

var ApplicationCall.userContext: UserContext?
    get() = attributes.getOrNull(CallAttributes.userContext)
    set(value) {
        when (value) {
            null -> attributes.remove(CallAttributes.userContext)
            else -> attributes.put(CallAttributes.userContext, value)
        }
    }

val ApplicationCall.userId: String?
    get() = userContext?.id

val ApplicationCall.userType: UserShared.Type?
    get() = userContext?.type

val ApplicationCall.isAdmin: Boolean
    get() = userContext?.type == UserShared.Type.Admin

val ApplicationCall.isApiRequest: Boolean
    get() {
        val path = request.path()
        if (path.startsWith("/api/")) return true
        val accept = request.headers[HttpHeaders.Accept].orEmpty()
        return accept.contains(ContentType.Application.Json.toString(), ignoreCase = true) &&
            !accept.contains(ContentType.Text.Html.toString(), ignoreCase = true)
    }
