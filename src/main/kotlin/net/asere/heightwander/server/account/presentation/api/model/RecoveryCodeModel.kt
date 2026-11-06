package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode
import net.asere.heightwander.server.account.presentation.api.AccountResponse

@Serializable
data class RecoveryCodeModel(
    val value: String,
) : AccountResponse

fun RecoveryCode.asApi() = RecoveryCodeModel(
    value = value,
)

fun RecoveryCode.toResponse(): Response<AccountResponse> = Response(
    body = asApi(),
)
