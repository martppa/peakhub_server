package net.asere.heightwander.server.account.presentation.api.model

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.NeedsTermsConfirmationError
import net.asere.heightwander.server.account.presentation.api.AccountResponse
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ApiError

@Serializable
class NeedsTermsConfirmationApiError :
    ApiError(
        code = "NeedsTermsConfirmation",
    ),
    AccountResponse

fun NeedsTermsConfirmationError.toResponse() = Response<AccountResponse>(
    code = 403,
    body = NeedsTermsConfirmationApiError(),
)
