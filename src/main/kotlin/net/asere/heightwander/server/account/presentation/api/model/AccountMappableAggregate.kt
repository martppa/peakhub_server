package net.asere.heightwander.server.account.presentation.api.model

import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.AccessDisabledError
import net.asere.heightwander.server.account.domain.model.mappable.AccountMappableAggregate
import net.asere.heightwander.server.account.domain.model.mappable.EmailNotVerifiedError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidCredentialsError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidEmailError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidPasswordError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidRecoveryCodeError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidTokenError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidUsernameError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidVerificationCodeError
import net.asere.heightwander.server.account.domain.model.mappable.NeedsTermsConfirmationError
import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode
import net.asere.heightwander.server.account.domain.model.mappable.Session
import net.asere.heightwander.server.account.domain.model.mappable.SocialLoginError
import net.asere.heightwander.server.account.domain.model.mappable.Success
import net.asere.heightwander.server.account.domain.model.mappable.UserAlreadyExistError
import net.asere.heightwander.server.account.domain.model.mappable.UserDoesNotExistError
import net.asere.heightwander.server.account.domain.model.mappable.WrongSourceError
import net.asere.heightwander.server.account.domain.model.mappable.toResponse
import net.asere.heightwander.server.account.presentation.api.AccountResponse

fun AccountMappableAggregate.toResponse(): Response<AccountResponse> = when (this) {
    AccessDisabledError -> AccessDisabledError.toResponse()
    EmailNotVerifiedError -> EmailNotVerifiedError.toResponse()
    InvalidCredentialsError -> InvalidCredentialsError.toResponse()
    InvalidRecoveryCodeError -> InvalidRecoveryCodeError.toResponse()
    InvalidTokenError -> InvalidTokenError.toResponse()
    InvalidUsernameError -> InvalidUsernameError.toResponse()
    InvalidVerificationCodeError -> InvalidVerificationCodeError.toResponse()
    NeedsTermsConfirmationError -> NeedsTermsConfirmationError.toResponse()
    SocialLoginError -> SocialLoginError.toResponse()
    UserAlreadyExistError -> UserAlreadyExistError.toResponse()
    UserDoesNotExistError -> UserDoesNotExistError.toResponse()
    WrongSourceError -> WrongSourceError.toResponse()
    InvalidEmailError -> InvalidEmailError.toResponse()
    InvalidPasswordError -> InvalidPasswordError.toResponse()
    is RecoveryCode -> this.toResponse()
    is Session -> this.toResponse()
    Success -> Success.toResponse()
}
