package net.asere.heightwander.server.account.presentation.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.Session
import net.asere.heightwander.server.account.presentation.api.AccountResponse

@Serializable
data class SessionModel(
    val userId: String,
    val token: String,
    val refresh: String,
) : AccountResponse

fun Session.asApi() = SessionModel(
    userId = userId,
    token = token,
    refresh = refresh,
)

fun Session.toResponse() = Response(
    body = asApi(),
)
