package net.asere.heightwander.server.account.presentation.api

import net.asere.omni.ktor.ExceptionResponseMapper
import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.AccessDisabledError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidCredentialsError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidEmailError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidPasswordError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidRecoveryCodeError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidTokenError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidUsernameError
import net.asere.heightwander.server.account.domain.model.mappable.NeedsTermsConfirmationError
import net.asere.heightwander.server.account.domain.model.mappable.SocialLoginError
import net.asere.heightwander.server.account.domain.model.mappable.UserAlreadyExistError
import net.asere.heightwander.server.account.domain.model.mappable.UserDoesNotExistError
import net.asere.heightwander.server.account.domain.model.mappable.WrongSourceError
import net.asere.heightwander.server.account.presentation.api.model.AccountUnknownApiError
import net.asere.heightwander.server.account.presentation.api.model.toResponse

class AccountExceptionMapper : ExceptionResponseMapper<AccountResponse> {
    override fun valueOf(input: Throwable): Response<AccountResponse> = when (input) {
        AccessDisabledError -> AccessDisabledError.toResponse()
        InvalidCredentialsError -> InvalidCredentialsError.toResponse()
        InvalidEmailError -> InvalidEmailError.toResponse()
        InvalidTokenError -> InvalidTokenError.toResponse()
        InvalidUsernameError -> InvalidUsernameError.toResponse()
        InvalidPasswordError -> InvalidPasswordError.toResponse()
        NeedsTermsConfirmationError -> NeedsTermsConfirmationError.toResponse()
        UserAlreadyExistError -> UserAlreadyExistError.toResponse()
        UserDoesNotExistError -> UserDoesNotExistError.toResponse()
        SocialLoginError -> SocialLoginError.toResponse()
        WrongSourceError -> WrongSourceError.toResponse()
        InvalidRecoveryCodeError -> InvalidRecoveryCodeError.toResponse()
        else -> AccountUnknownApiError().toResponse()
    }
}
